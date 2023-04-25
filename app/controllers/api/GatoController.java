package controllers.api;
import com.fasterxml.jackson.databind.JsonNode;
import exceptions.ApiError;
import models.Gato;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Result;

import static play.mvc.Results.*;

public class GatoController {
    private static final Logger.ALogger apiLogger = Logger.of("controllers.api.DebGato");


    public static Result getGatos() {
        try {
            JsonNode serialized = Json.toJson(Gato.findAll());
            return ok(serialized);
        } catch (Exception e) {
            apiLogger.error("[getlist]Ocurrió un error al obtener gatos", e);
            JsonNode response = ApiError.initError(ApiError.EErrorKey.UnexpectedError);
            return internalServerError(response);
        }
    }


    public static Result createGato() {
        try {
            // Get the form from the request
            Form<Gato> form = Form.form(models.Gato.class).bindFromRequest();

            if (form.hasErrors()) {
                JsonNode validationErrors = ApiError.rebuildErrors(form.errors(), models.Service.class.getSimpleName());
                JsonNode jsonError = ApiError.initError(ApiError.EErrorKey.ValidationError, validationErrors.toString());
                return badRequest(jsonError);
            }

            // Get the object from the form
            Gato gato = form.get();

            Gato.create(gato);
            return ok();

        } catch (Exception e) {
            apiLogger.error("[create]No se pudo crear el gato", e);
            JsonNode response = ApiError.initError(ApiError.EErrorKey.UnexpectedError);
            return internalServerError(response);
        }
    }


    public static Result delete(Long id) {
        try {
            //consultar por metodo finder
            Gato gato = Gato.findById(id);
            if (gato != null) {
                Gato.delete(gato);
            }else {
                apiLogger.error("[delete] El gato no existe");
                return badRequest("No se pudo crear porque el gato no existe");
            }
            return ok();
        } catch (Exception e) {
            apiLogger.error("[delete] Ocurrió un error al borrar el gato", e);
            JsonNode response = ApiError.initError(ApiError.EErrorKey.UnexpectedError);
            return internalServerError(response);
        }

    }

    public static Result getById(Long id) {
        try {
            JsonNode serialized = null;
            Gato gato = Gato.findById(id);
            if (gato != null)  serialized = Json.toJson(gato);
            else {
                apiLogger.error("[get]El gato no existe");
                JsonNode response = ApiError.initError(ApiError.EErrorKey.NotFound);
                return notFound(response);
            }
            return ok(serialized);
        } catch (Exception e) {
            apiLogger.error("[get] Ocurrió un error al obtener el gato", e);
            JsonNode response = ApiError.initError(ApiError.EErrorKey.UnexpectedError);
            return internalServerError(response);
        }
    }

    public static Result updateGato() {
        try {
            // Get the form from the request
            Form<Gato> form = Form.form(models.Gato.class).bindFromRequest();

            if (form.hasErrors()) {
                JsonNode validationErrors = ApiError.rebuildErrors(form.errors(), models.Service.class.getSimpleName());
                JsonNode jsonError = ApiError.initError(ApiError.EErrorKey.ValidationError, validationErrors.toString());
                return badRequest(jsonError);
            }
            // Get the object from the form
            Gato gato = form.get();
            Gato.update(gato);
            return ok();
        } catch (Exception e) {
            apiLogger.error("[update] Ocurrió un error al updatear gato", e);
            JsonNode response = ApiError.initError(ApiError.EErrorKey.UnexpectedError);
            return internalServerError(response);
        }
    }



}