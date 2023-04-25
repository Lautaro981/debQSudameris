package controllers;

import annotations.bErrorCatcher;
import com.fasterxml.jackson.databind.JsonNode;
import dto.*;
import dto.Error;
import exceptions.ApiError;
import helpers.SudamerisResponse;
import play.Logger;
import play.libs.F;
import play.libs.Json;
import play.mvc.*;

import java.util.ArrayList;
import java.util.List;

public class Application extends Controller {

    private final static Logger.ALogger logger = Logger.of("connector.controllers.application");

    public static Result index() {
        return ok(views.html.index.render("Conector Debmedia - Sudameris"));
    }

    @bErrorCatcher
    public static F.Promise<Result> getCustomer() {
        JsonNode request = request().body().asJson();

        logger.debug("Request debQ: " + request.toString());

        String data = null;
        String cedulaIdentidad = null;
        if(request.has("cedulaIdentidad")) {
            cedulaIdentidad = request.get("cedulaIdentidad").asText();
        }
         else if (request.has("dni")) {
            data = request.get("dni").asText();
        }
        else {
            logger.error("La request no tiene el campo dni o el campo cedulaIdentidad.");
            return F.Promise.pure(badRequest(ApiError.initError(ApiError.EErrorKey.BadParameters, "La request no tiene el campo dni o el campo cedulaIdentidad.")));
        }


        String finalData = data;
        String finalCedulaIdentidad = cedulaIdentidad;
        return getByDoc(data, cedulaIdentidad).recover(throwable -> {
            logger.error("[getCustomer] Error", throwable);
            return getCustomerVacioResult(finalData, finalCedulaIdentidad);
        });

    }

    private static F.Promise<Result> getByDoc(String doc, String cedulaIdentidad) {

        try {
            F.Promise<String> tokenPromise = Services.getToken();

            tokenPromise.recover(throwable -> {
                logger.error("Error obteniendo el token.");
                throw throwable;
            });

            return tokenPromise.flatMap(token -> {
                SudamerisResponse sudamerisResponse = new SudamerisResponse();
                if (doc != null) {
                    sudamerisResponse.getCustomer().setDni(doc);
                } else if (cedulaIdentidad != null) {
                    sudamerisResponse.getCustomer().setCedulaIdentidad(cedulaIdentidad);
                }


                try {
                    return Services.getSegmentoPersonaDTO(doc, token, cedulaIdentidad).recover(throwable -> {
                        logger.error("Error en SegmentoPersona.");
                        throw throwable;
                    }).flatMap(segmentoPersonaDTO -> {
                        sudamerisResponse.mapSegmentoPersona(segmentoPersonaDTO);

                        return Services.getChequeraPendienteRetiro(doc,token, cedulaIdentidad).recover(throwable -> {
                            logger.error("Error en ChequeraPendienteRetiro.");
                            throw throwable;
                        }).flatMap(chequeraPendienteRetiroDTO -> {
                            sudamerisResponse.mapChequeraPendienteRetiro(chequeraPendienteRetiroDTO);

                            return Services.getTarjetaPendienteEntrega(doc, token, cedulaIdentidad).recover(throwable -> {
                                logger.error("Error en TarjetaPendienteEntregaDTO.");
                                throw throwable;
                            }).flatMap(tarjetaPendienteEntregaDTO -> {
                                sudamerisResponse.mapTarjetaPendienteEntrega(tarjetaPendienteEntregaDTO);
                                List<Customer> customerList = new ArrayList<>();
                                customerList.add(sudamerisResponse.getCustomer());
                                JsonNode serialized = Json.toJson(customerList);
                                logger.debug("getCustomer json response: " + serialized);
                                return F.Promise.pure(ok(serialized));
                            });
                        });
                    });
                } catch (Exception e) {
                    logger.error("Error obteniendo las promises de los servicios.");
                    return F.Promise.pure(getCustomerVacioResult(doc, cedulaIdentidad));
                }
            });
        } catch (Exception e) {
            logger.error("Error en getByDoc.");
            return F.Promise.pure(getCustomerVacioResult(doc, cedulaIdentidad));
        }
    }

    private static Customer customerVacio(String doc, String cedulaIdentidad) {
        Customer customer = new Customer(doc, cedulaIdentidad);
        customer.setCustomerTypeDefault();
        logger.error("Ocurrió un error. Se crea customer vacío con numero de documento " + doc + cedulaIdentidad);
        return customer;
    }

    private static Result getCustomerVacioResult(String doc, String cedulaIdentidad) {
        Customer customerVacio = customerVacio(doc, cedulaIdentidad);
        List<Customer> customerList = new ArrayList<>();
        customerList.add(customerVacio);
        JsonNode serialized = Json.toJson(customerList);
        logger.debug("getCustomer json response: " + serialized);
        return ok(serialized);
    }
}
