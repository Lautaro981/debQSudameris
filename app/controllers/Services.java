package controllers;

import com.debmedia.utils.controllers.WS.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.WS.PlayWSWrapperWithLogger;
import dto.*;
import models.Attribute;
import models.Service;
import models.Token;
import play.Logger;
import play.libs.F;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static play.libs.Crypto.decryptAES;

public class Services extends Controller {

    private final static Logger.ALogger logger = Logger.of("connector.controllers.services");
    public static Map<String, Map<String, String>> configs;

    public static void loadMapInRAM() {
        // configs is a public static map defined in Application.java
        Services.configs = new HashMap<>();
        // LOAD DE MAP FROM DB
        List<Service> services = Service.getList();
        // Iterar services y agregarlos al map
        Map<String, String> serviceConfigs;
        for (Service service : services) {
            serviceConfigs = new HashMap<>();
            for (Attribute attribute : service.getAttributes()) {
                serviceConfigs.put(attribute.getAttribute(), attribute.getAttributeValue());
            }
            Services.configs.put(service.getName(), serviceConfigs);
        }
    }

    public static Promise<SegmentoPersonaDTO> getSegmentoPersonaDTO(String doc, String token, String cedulaIdentidad) throws Exception {
        String url;
        Long timeout;

        try {
            Map<String, String> customerService = configs.get("segmentoPersona");
            url = customerService.get("url");
            timeout = Long.parseLong(customerService.get("timeout"));
        } catch (Exception e) {
            logger.error("Error al obtener el servicio segmentoPersona", e);
            throw e;
        }

        PlayWSWrapper ws = getPlayWSWrapper(doc, url, "SegmentoPersona", token, cedulaIdentidad);

        logger.debug("[getSegmentoPersonaDTO] Inicio la consulta de SegmentoPersona.");
        logger.debug("[getSegmentoPersonaDTO] Request: " + ws.toString());

        Promise<PlayWSResponseWrapper> response = executeWithLoginRetry(timeout, ws);

        return response.map(segmentoPersonaResponse -> {
            GenericBody responseBody = segmentoPersonaResponse.getBody();
            JsonBody jsonResponseBody;
            try {
                jsonResponseBody = (JsonBody) responseBody;
            } catch (Exception e) {
                Logger.of("application").error("Error parsing segmentoPersona response as json. Service response was: " + responseBody.getBodyAsString(), e);
                throw new RuntimeException("Error parsing response as json", e);
            }

            logger.debug("[getSegmentoPersonaDTO] La respuesta del servicio SegmentoPersona fué: " + segmentoPersonaResponse.toString());

            SegmentoPersonaDTO segmentoPersonaDTO;
            segmentoPersonaDTO = SegmentoPersonaDTO.mapFromServiceResponse(jsonResponseBody.getBody());
            return segmentoPersonaDTO;
        });
    }

    private static boolean hasInvalidSession(JsonBody jsonResponseBody) {
        ArrayNode errors = (ArrayNode) jsonResponseBody.getBody().get("Erroresnegocio").get("BTErrorNegocio");
        for (JsonNode error : errors) {
            if (error.get("Codigo").asText().equals("10011"))
                return true;
        }
        return false;
    }

    public static F.Promise<PlayWSResponseWrapper> executeWithLoginRetry(Long timeOutInMilis, PlayWSWrapper ws) {
        return ws.execute(timeOutInMilis == null ? 10000L : timeOutInMilis).flatMap(sudamerisResponse -> verifyMethodResponse(sudamerisResponse, ws));
    }

    public static Promise<PlayWSResponseWrapper> verifyMethodResponse(PlayWSResponseWrapper sudamerisResponse, PlayWSWrapper ws) {
        GenericBody responseBody = sudamerisResponse.getBody();
        JsonBody jsonResponseBody;
        try {
            jsonResponseBody = (JsonBody) responseBody;
        } catch (Exception e) {
            Logger.of("application").error("Error parsing response as json. Service response was: " + responseBody.getBodyAsString(), e);
            throw new RuntimeException("Error parsing response as json", e);
        }

        try {
            if (hasInvalidSession(jsonResponseBody)) {
                Logger.of("application").debug("Invalid session. Service response was: " + sudamerisResponse.toString());
                return retry(ws);
            }
        } catch (Exception e) {
            Logger.of("application").error("Fail finding errors in response. Service response was: " + jsonResponseBody.getBodyAsString());
            throw new RuntimeException("Fail finding errors in response", e);
        }
        return F.Promise.pure(sudamerisResponse);
    }

    private static Promise<PlayWSResponseWrapper> retry(PlayWSWrapper ws) throws Exception {
        Logger.of("application").debug("Volviendo a autenticar");
        return updateToken().flatMap(token -> {
            PlayWSWrapper wsRetry = replaceToken(ws, token);
            return wsRetry.execute();
        });
    }

    private static PlayWSWrapper replaceToken(PlayWSWrapper ws, String token) throws Exception {
        JsonBody body = (JsonBody) ws.getBody();
        JsonNode jsonBody = body.getBody();
        String doc = jsonBody.get("Parametros").get("Parametro").get("sBTRepParametros.It").get(0).get("Valor").asText();
        Map<String, String[]> queryStrings = ws.getQueryStrings();
        String queryParam = null;
        if(queryStrings!=null && !queryStrings.isEmpty()){
            queryParam = queryStrings.keySet().iterator().next();
        }
        String cedulaIdentidad = null;
        return getPlayWSWrapper(doc, ws.getUrl(), queryParam, token, cedulaIdentidad);
    }

    private static PlayWSWrapper getPlayWSWrapper(String doc, String url, String queryParam, String token, String cedulaIdentidad) throws Exception {
        ObjectNode btinreq = getBtinreq(token);
        ObjectNode params = getParameters(doc, cedulaIdentidad);

        ObjectNode form = JsonNodeFactory.instance.objectNode();
        form.put("Btinreq", btinreq);
        form.put("Parametros", params);

        HashMap<String, String[]> queryParams = new HashMap<>();
        if(queryParam!=null && !"".equals(queryParam))
            queryParams.put(queryParam, new String[]{null});

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return new PlayWSWrapper(PlayWSWrapper.HttpVerb.POST, url, headers, queryParams, new JsonBody(form));
    }


    public static Promise<String> getToken() throws Exception {

        Token token = Token.get();

        if (token.getValue() != null) {
            if (token.isFromToday()) {
                return Promise.pure(token.getValue());
            }
            Logger.of("application").debug("El token no es del día.");
        }

        Logger.of("application").debug("Volviendo a obtener el token, token actual:"+token);
        return updateToken();
    }

    private static Promise<String> updateToken() throws Exception {
        String url, user, pass, queryParam;
        Long timeout;

        try {
            Map<String, String> customerService = configs.get("authentication");
            url = customerService.get("url");
            user = customerService.get("userId");
            pass = decryptAES(customerService.get("password"));
            timeout = Long.parseLong(customerService.get("timeout"));
        } catch (Exception e) {
            logger.error("Error al obtener el servicio authenticate", e);
            throw e;
        }

        ObjectNode btinreq = getBtinreq("");

        ObjectNode auth_form = JsonNodeFactory.instance.objectNode();
        auth_form.put("Btinreq", btinreq);
        auth_form.put("UserId", user);
        auth_form.put("UserPassword", pass);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        HashMap<String, String[]> queryParams = new HashMap<>();
        queryParams.put("Execute", new String[]{null});

        PlayWSWrapper ws = new PlayWSWrapper(PlayWSWrapper.HttpVerb.POST, url, headers, queryParams, new JsonBody(auth_form));
        logger.debug("[updateToken] Inicio la autenticación.");
        logger.debug("[updateToken] Request: " + ws.toString());

        F.Promise<PlayWSResponseWrapper> response = ws.execute(timeout);

        return response.map(playWSResponseWrapper -> {
            logger.debug("[updateToken] Response del servicio Authenticate: " + playWSResponseWrapper.toString());
            Token.updateValue(playWSResponseWrapper);
            return Token.get().getValue();
        });
    }

    private static ObjectNode getBtinreq(String token) throws Exception {

        String template, user, ip;

        try {
            Map<String, String> generalService = configs.get("general");
            template = generalService.get("Template Btinreq");
            ip = generalService.get("ip");

            Map<String, String> authService = configs.get("authentication");
            user = authService.get("userId");

        } catch (Exception e) {
            logger.error("Error al obtener el servicio authenticate", e);
            throw e;
        }

        TemplateBuilder templateBuilder = new TemplateBuilder(template);
        templateBuilder.replaceKey("device", ip);
        templateBuilder.replaceKey("usuario", user);
        templateBuilder.replaceKey("token", token);

        JsonNode jsonNode = Json.toJson(templateBuilder.getTemplate());
        ObjectNode objectNode = (ObjectNode) new ObjectMapper().readTree(jsonNode.asText());
        return objectNode;
    }

    private static ObjectNode getParameters(String doc, String cedulaIdentidad) throws Exception {
        String template;

        try {
            Map<String, String> generalService = configs.get("general");
            template = generalService.get("Template Parametros");
        } catch (Exception e) {
            logger.error("Error al obtener el servicio general", e);
            throw e;
        }

        TemplateBuilder templateBuilder = new TemplateBuilder(template);
        if (doc == null) {
            templateBuilder.replaceKey("valorDoc", cedulaIdentidad);
        } else {
            templateBuilder.replaceKey("valorDoc", doc);
        }


        JsonNode jsonNode = Json.toJson(templateBuilder.getTemplate());
        ObjectNode parametro = (ObjectNode) new ObjectMapper().readTree(jsonNode.asText());

        return parametro;
    }

    public static Promise<ChequeraPendienteRetiroDTO> getChequeraPendienteRetiro(String doc, String token, String cedulaIdentidad) throws Exception {
        String url;
        Long timeout;

        try {
            Map<String, String> customerService = configs.get("chequeraPendienteRetiro");
            url = customerService.get("url");
            timeout = Long.parseLong(customerService.get("timeout"));
        } catch (Exception e) {
            logger.error("Error al obtener el servicio chequeraPendienteRetiro", e);
            throw e;
        }

        PlayWSWrapper ws = getPlayWSWrapper(doc, url, "ChequeraPendRetiro", token, cedulaIdentidad);

        logger.debug("[getChequeraPendienteRetiro] Inicio la consulta de chequeraPendienteRetiro.");
        logger.debug("[getChequeraPendienteRetiro] Request: " + ws.toString());

        Promise<PlayWSResponseWrapper> response = executeWithLoginRetry(timeout, ws);

        return response.map(chequeraPendienteRetiroResponse -> {
            GenericBody responseBody = chequeraPendienteRetiroResponse.getBody();
            JsonBody jsonResponseBody;
            try {
                jsonResponseBody = (JsonBody) responseBody;
            } catch (Exception e) {
                Logger.of("application").error("Error parsing chequeraPendienteRetiro response as json. Service response was: " + responseBody.getBodyAsString(), e);
                throw new RuntimeException("Error parsing response as json", e);
            }

            logger.debug("[getChequeraPendienteRetiro] La respuesta del servicio chequeraPendienteRetiro fué: " + chequeraPendienteRetiroResponse.toString());

            ChequeraPendienteRetiroDTO chequeraPendienteRetiroDTO;
            chequeraPendienteRetiroDTO = ChequeraPendienteRetiroDTO.mapFromServiceResponse(jsonResponseBody.getBody());
            return chequeraPendienteRetiroDTO;
        });
    }

    public static Promise<TarjetaPendienteEntregaDTO> getTarjetaPendienteEntrega(String doc, String token, String cedulaIdentidad) throws Exception {
        String url;
        Long timeout;

        try {
            Map<String, String> customerService = configs.get("tarjetaPendienteEntrega");
            url = customerService.get("url");
            timeout = Long.parseLong(customerService.get("timeout"));
        } catch (Exception e) {
            logger.error("Error al obtener el servicio tarjetaPendienteEntrega", e);
            throw e;
        }

        PlayWSWrapper ws = getPlayWSWrapper(doc, url, "TarjPendEntrega",token, cedulaIdentidad);

        logger.debug("[getTarjetaPendienteEntrega] Inicio la consulta de tarjetaPendienteEntrega.");
        logger.debug("[getTarjetaPendienteEntrega] Request: " + ws.toString());

        Promise<PlayWSResponseWrapper> response = executeWithLoginRetry(timeout, ws);

        return response.map(tarjetaPendienteEntregaResponse -> {
            GenericBody responseBody = tarjetaPendienteEntregaResponse.getBody();
            JsonBody jsonResponseBody;
            try {
                jsonResponseBody = (JsonBody) responseBody;
            } catch (Exception e) {
                Logger.of("application").error("Error parsing tarjetaPendienteEntrega response as json. Service response was: " + responseBody.getBodyAsString(), e);
                throw new RuntimeException("Error parsing response as json", e);
            }

            logger.debug("[getTarjetaPendienteEntrega] La respuesta del servicio tarjetaPendienteEntrega fué: " + tarjetaPendienteEntregaResponse.toString());

            TarjetaPendienteEntregaDTO tarjetaPendienteEntregaDTO;
            tarjetaPendienteEntregaDTO = TarjetaPendienteEntregaDTO.mapFromServiceResponse(jsonResponseBody.getBody());
            return tarjetaPendienteEntregaDTO;
        });
    }
}
