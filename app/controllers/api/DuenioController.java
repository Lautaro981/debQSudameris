package controllers.api;

import com.fasterxml.jackson.databind.JsonNode;
import exceptions.ApiError;
import models.Duenio;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class DuenioController extends Controller {
    private static final Logger.ALogger apiLogger = Logger.of("controllers.api.DebDuenio");

    public static Result getDuenios() {
        try {
            JsonNode serialized= Json.toJson(Duenio.findAll());
            return ok(serialized);
        } catch (Exception e) {
            apiLogger.error("[getlist]Ocurrió un error al obtener dueños", e);
            JsonNode response = ApiError.initError(ApiError.EErrorKey.UnexpectedError);
            return internalServerError(response);

        }
    }


    public static Result createDuenio(){
        try {
            // Get the form from the request
            Form<Duenio> form = Form.form(models.Duenio.class).bindFromRequest();

            if (form.hasErrors()) {
                JsonNode validationErrors = ApiError.rebuildErrors(form.errors(), models.Service.class.getSimpleName());
                JsonNode jsonError = ApiError.initError(ApiError.EErrorKey.ValidationError, validationErrors.toString());
                return badRequest(jsonError);
            }

            // Get the object from the form
            Duenio duenio= form.get();
            Duenio.create(duenio);
            return ok();

        }catch(Exception e){
            apiLogger.error("[create]Ocurrió un error al crear dueño", e);
            JsonNode response = ApiError.initError(ApiError.EErrorKey.UnexpectedError);
            return internalServerError(response);
        }
    }


    public static Result delete(Long id){
        try {
            Duenio duenio= Duenio.findById(id);
            if(duenio!=null) {
                Duenio.delete(duenio);
            } else {
                apiLogger.error("[delete]No se pudo borrar porque el dueño no existe");
                return badRequest("No se pudo borrar porque el dueño no existe");
            }
            return ok();
        }catch(Exception e){
            apiLogger.error("[delete]Ocurrió un error al borrar dueños", e);
            JsonNode response = ApiError.initError(ApiError.EErrorKey.UnexpectedError);
            return internalServerError(response);
        }

    }

    public static Result getById(Long id){
        try {
            JsonNode serialized=null;
            Duenio duenio=Duenio.findById(id);
            if(duenio!=null) serialized=Json.toJson(duenio);
            else {
                apiLogger.error("[get]El dueño no existe");
                JsonNode response = ApiError.initError(ApiError.EErrorKey.NotFound);
                return notFound(response);
            }
            return ok(serialized);
        }catch(Exception e){
            apiLogger.error("[get]Ocurrió un error al obtener dueño por id", e);
            JsonNode response = ApiError.initError(ApiError.EErrorKey.UnexpectedError);
            return internalServerError(response);
        }
    }

    public static Result updateDuenio() {
        try {
            // Get the form from the request
            Form<Duenio> form = Form.form(models.Duenio.class).bindFromRequest();

            if (form.hasErrors()) {
                JsonNode validationErrors = ApiError.rebuildErrors(form.errors(), models.Service.class.getSimpleName());
                JsonNode jsonError = ApiError.initError(ApiError.EErrorKey.ValidationError, validationErrors.toString());
                return badRequest(jsonError);
            }
            // Get the object from the form
            Duenio duenio = form.get();
            Duenio.update(duenio);
            return ok();
        } catch (Exception e) {
            apiLogger.error("[update]Ocurrió un error al updatear dueño", e);
            JsonNode response = ApiError.initError(ApiError.EErrorKey.UnexpectedError);
            return internalServerError(response);

        }
    }
}
