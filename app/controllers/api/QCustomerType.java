package controllers.api;

import com.fasterxml.jackson.databind.JsonNode;
import exceptions.ApiError;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

public class QCustomerType extends Controller {

    private static final Logger.ALogger apiLogger = Logger.of("controllers.api.QCustomerType");

    public static Result list() {
        try {
            List<models.QCustomerType> customers = models.QCustomerType.getList();
            JsonNode serialized = Json.toJson(customers);
            return ok(serialized);
        } catch (Exception e) {
            apiLogger.error("[list] Ocurrió un error inesperado durante el armado de la lista de QCustomersTypes.", e);
            JsonNode response = ApiError.initError(ApiError.EErrorKey.UnexpectedError);
            return internalServerError(response);
        }
    }

    public static Result retrieve(Long id) {
        try {
            models.QCustomerType qCustomerType = models.QCustomerType.findById(id);
            JsonNode serialized = Json.toJson(qCustomerType);
            return ok(serialized);
        } catch (Exception e) {
            apiLogger.error("[retrieve] Ocurrió un error inesperado durante la obtención del QCustomersType.", e);
            JsonNode response = ApiError.initError(ApiError.EErrorKey.UnexpectedError);
            return internalServerError(response);
        }
    }

    public static Result delete(Long id) {
        try {
            models.QCustomerType customerType = models.QCustomerType.findById(id);
            if (customerType == null) {
                apiLogger.warn("[delete] No se encontró el QCustomerType.");
                JsonNode response = ApiError.initError(ApiError.EErrorKey.BadContext);
                return badRequest(response);
            }
            if (customerType.getLabel().equals("No cliente")) {
                apiLogger.warn("[delete] No se puede eliminar el tipo de cliente default.");
                JsonNode response = ApiError.initError(ApiError.EErrorKey.BadContext);
                return badRequest(response);
            }
            models.QCustomerType.delete(customerType);
            return ok();
        } catch (Exception e) {
            apiLogger.error("[delete] Ocurrió un error inesperado durante la eliminación, el QCustomersTypes no fue eliminado.", e);
            JsonNode response = ApiError.initError(ApiError.EErrorKey.UnexpectedError);
            return internalServerError(response);
        }
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result update(Long id) {

        Form<models.QCustomerType> qCustomerTypeForm = Form.form(models.QCustomerType.class, models.QCustomerType.updating.class).bindFromRequest();

        if (qCustomerTypeForm.hasErrors()) {
            apiLogger.error("update: " + qCustomerTypeForm.errorsAsJson());
            return badRequest(qCustomerTypeForm.errorsAsJson());
        }

        models.QCustomerType qCustomerType = qCustomerTypeForm.get();

        try {
            models.QCustomerType.update(qCustomerType);
            JsonNode serialized = Json.toJson(qCustomerType);
            return ok(serialized);
        } catch (Exception e) {
            apiLogger.error("[update] Ocurrió un error inesperado durante la edición del QCustomersType.", e);
            JsonNode response = ApiError.initError(ApiError.EErrorKey.UnexpectedError);
            return internalServerError(response);
        }
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result create() {
        Form<models.QCustomerType> customerTypeForm = Form.form(models.QCustomerType.class, models.QCustomerType.creating.class).bindFromRequest();

        if (customerTypeForm.hasErrors()) {
            apiLogger.error("[create]: " + customerTypeForm.errorsAsJson());
            return badRequest(customerTypeForm.errorsAsJson());
        }

        models.QCustomerType customerType = customerTypeForm.get();

        try {
            models.QCustomerType.create(customerType);
            JsonNode serialized = Json.toJson(customerType);
            return created(serialized);
        } catch (Exception e) {
            apiLogger.error("[create] Ocurrió un error inesperado durante la creación del QCustomersType.", e);
            JsonNode response = ApiError.initError(ApiError.EErrorKey.UnexpectedError);
            return internalServerError(response);
        }
    }
}
