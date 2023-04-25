package controllers.api;

import com.debmedia.utils.models.debModel;
import com.fasterxml.jackson.databind.JsonNode;
import exceptions.ApiError;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

public class Rule extends Controller {
    private static final Logger.ALogger apiLogger = Logger.of("controllers.api.Rule");

    public static Result list() {
        try {
            List<models.Rule> rules = models.Rule.getList();
            JsonNode serialized = Json.toJson(rules);
            return ok(serialized);
        } catch (Exception e) {
            apiLogger.error("[list] Ocurrió un error inesperado durante el armado de la lista de Rules.", e);
            JsonNode response = ApiError.initError(ApiError.EErrorKey.UnexpectedError);
            return internalServerError(response);
        }
    }

    public static Result retrieve(Long id) {
        try {
            models.Rule rule = models.Rule.findById(id);
            JsonNode serialized = Json.toJson(rule);
            return ok(serialized);
        } catch (Exception e) {
            apiLogger.error("[retrieve] Ocurrió un error inesperado durante la obtención de la rule.", e);
            JsonNode response = ApiError.initError(ApiError.EErrorKey.UnexpectedError);
            return internalServerError(response);
        }
    }

    public static Result delete(Long id) {
        try {
            models.Rule rule = models.Rule.findById(id);
            if (rule == null) {
                apiLogger.warn("[delete] No se encontró la rule.");
                JsonNode response = ApiError.initError(ApiError.EErrorKey.BadContext);
                return badRequest(response);
            }
            try{
                debModel.getServer().beginTransaction();
                rule.delete();
                debModel.getServer().commitTransaction();
            }
            catch (Exception e){
                debModel.getServer().rollbackTransaction();
                throw e;
            }
            finally {
                debModel.getServer().endTransaction();
            }
            return ok();
        } catch (Exception e) {
            apiLogger.error("[delete] Ocurrió un error inesperado durante la eliminación, la rule no fue eliminada.", e);
            JsonNode response = ApiError.initError(ApiError.EErrorKey.UnexpectedError);
            return internalServerError(response);
        }
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result create() {
        Form<models.Rule> ruleForm = Form.form(models.Rule.class, models.Rule.creating.class).bindFromRequest();

        if (ruleForm.hasErrors()) {
            apiLogger.error("[create]: " + ruleForm.errorsAsJson());
            return badRequest(ruleForm.errorsAsJson());
        }

        models.Rule rule = ruleForm.get();

        try {
            rule.create();
            JsonNode serialized = Json.toJson(rule);
            return created(serialized);
        } catch (Exception e) {
            apiLogger.error("[create] Ocurrió un error inesperado durante la creación de la rule.", e);
            JsonNode response = ApiError.initError(ApiError.EErrorKey.UnexpectedError);
            return internalServerError(response);
        }
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result update(Long id) {

        Form<models.Rule> ruleForm = Form.form(models.Rule.class, models.Rule.updating.class).bindFromRequest();

        if (ruleForm.hasErrors()) {
            apiLogger.error("update: " + ruleForm.errorsAsJson());
            return badRequest(ruleForm.errorsAsJson());
        }

        models.Rule rule = ruleForm.get();

        try {
            try{
                debModel.getServer().beginTransaction();
                rule.update();
                debModel.getServer().commitTransaction();
            }
            catch (Exception e){
                debModel.getServer().rollbackTransaction();
                throw e;
            }
            finally {
                debModel.getServer().endTransaction();
            }
            JsonNode serialized = Json.toJson(rule);
            return ok(serialized);
        } catch (Exception e) {
            apiLogger.error("[update] Ocurrió un error inesperado durante la edición de la rule.", e);
            JsonNode response = ApiError.initError(ApiError.EErrorKey.UnexpectedError);
            return internalServerError(response);
        }
    }
}
