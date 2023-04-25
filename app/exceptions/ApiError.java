package exceptions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.data.validation.ValidationError;
import play.i18n.Messages;

import java.util.List;
import java.util.Map;

/**
 * @author Diego Esposito
 */
public class ApiError {
    public enum EErrorKey {
        LicenceExpiration,
        UnexpectedError,
        CreationError,
        InvalidJson,
        BadParameters,
        ValidationError,
        InvalidBody,
        InvalidRoute,
        Unauthorized,
        Forbidden,
        BadContext,
        ActiveTransaction,
        NotFound,
        BusyWorker,
        IdleWorker,
        EmptyGroup,
        TurnNotFound,
        CalledTurn,
        NetworkConnectionFailed

    }

    private String message;
    private JsonNode jsonMessage;

    private EErrorKey key;

    /**
     * Safe serialization of the class
     *
     * @return
     */
    public ObjectNode jsonSerialization() {
        ObjectNode auxError = JsonNodeFactory.instance.objectNode();
        if (this.getEErrorKey() != null) {
            auxError.put("key", this.getEErrorKey().name());
        }
        if (this.getMessage() != null) {
            auxError.put("message", this.getMessage());
        } else if (this.getJsonMessage() != null) {
            auxError.put("message", this.getJsonMessage());
        }
        return auxError;
    }

    public static JsonNode initError(EErrorKey key, String message) {
        ApiError error = new ApiError();
        error.setEErrorKey(key);
        error.setMessage(message);
        JsonNode jsonError = error.jsonSerialization();
        return jsonError;
    }

    public static JsonNode initError(EErrorKey key, JsonNode message) {
        ApiError error = new ApiError();
        error.setEErrorKey(key);
        error.setJsonMessage(message);
        ObjectNode jsonError = error.jsonSerialization();
        return jsonError;
    }

    public static JsonNode initError(EErrorKey key) {
        ApiError error = new ApiError();
        error.setEErrorKey(key);
        ObjectNode jsonError = error.jsonSerialization();
        return jsonError;
    }

    /**
     * This method rebuilds the errors from a given model using internationalization
     *
     * @param Errors
     * @param className
     * @return
     */
    public static ObjectNode rebuildErrors(Map<String, List<ValidationError>> Errors, String className) {
        //Creo un objecto json que contendrá los errores en formato json
        ObjectNode jsonErrors = JsonNodeFactory.instance.objectNode();
        //Recorro el mapa de errores
        for (Map.Entry<String, List<ValidationError>> propertie : Errors.entrySet()) {
            //Obtengo la propiedad que posee errores
            String key = propertie.getKey();
            //Obtengo todos los errores de la propiedad
            List<ValidationError> fieldErrors = propertie.getValue();
            //Creo un jsonArray que contendrá todos los errores de dicha propiedad
            ArrayNode jsonFieldErros = JsonNodeFactory.instance.arrayNode();
            for (ValidationError error : fieldErrors) {
                //Obtengo el tipo de error
                String typeError = error.message();
                //Construyo el string que me servira para buscar el mensaje internacionalizado
                String messageKey = className + "." + key + "." + typeError;
                //Obtengo el mensaje internacionalizado
                String finalError = Messages.get(messageKey);
                //Si no lo encontró, entonces messageKey queda con el mismo string con el cual se busco
                if (!finalError.equals(messageKey)) {
                    jsonFieldErros.add(finalError);
                } else {
                    //Busco un mensaje mas generico
                    String aux = Messages.get(typeError);
                    //Chequeo que haya encontrado el mensaje
                    if (!aux.equals(typeError)) {
                        //Si lo encontro lo agrego
                        jsonFieldErros.add(aux);
                    } else {
                        //Si no lo encontro coloco solo el mensaje error
                        jsonFieldErros.add("error");
                    }
                }
            }
            //Coloco en el objecto json el jsonArray usando la key de la propiedad
            jsonErrors.put(key, jsonFieldErros);
        }
        //Devuelvo todos los errores
        return jsonErrors;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Enum getEErrorKey() {
        return key;
    }

    public void setEErrorKey(EErrorKey key) {
        this.key = key;
    }

    public JsonNode getJsonMessage() {
        return jsonMessage;
    }

    public void setJsonMessage(JsonNode jsonMessage) {
        this.jsonMessage = jsonMessage;
    }

}
