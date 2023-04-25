package controllers.api;

import com.debmedia.utils.controllers.Serializer;
import com.debmedia.utils.models.debModel;
import com.fasterxml.jackson.databind.JsonNode;
import controllers.Services;
import exceptions.ApiError;
import play.Logger;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Arrays;
import java.util.List;

public class Service extends Controller {

    private static final Logger.ALogger apiLogger = Logger.of("connector.controllers.api.cService");
    private static final List<String> serializationFields
            = Arrays.asList("id", "name", "attributes.id", "attributes.attribute", "attributes.attributeValue");

    public static Result list() {
        try {
            List<models.Service> services = models.Service.getList();
            JsonNode serialized = Serializer.serialize(services, serializationFields);
            return ok(serialized);
        } catch (Exception e) {
            apiLogger.error("[list] Ocurrió un error inesperado durante el armado de la lista de services.", e);
            JsonNode response = ApiError.initError(ApiError.EErrorKey.UnexpectedError);
            return internalServerError(response);
        }
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result create() {
        // Get the form from the request
        Form<models.Service> form = Form.form(models.Service.class, models.Service.creating.class).bindFromRequest();
        // Validate errors
        if (form.hasErrors()) {
            JsonNode validationErrors = ApiError.rebuildErrors(form.errors(), models.Service.class.getSimpleName());
            JsonNode jsonError = ApiError.initError(ApiError.EErrorKey.ValidationError, validationErrors.toString());
            apiLogger.error("Service.create: " + jsonError);
            return badRequest(jsonError);
        }

        // Get the object from the form
        models.Service service = form.get();

        // Create the object in db
        debModel.getServer().beginTransaction();
        try {
            models.Service.create(service);
            JsonNode jsonObject = Serializer.serialize(service, serializationFields);
            debModel.getServer().commitTransaction();
            Services.loadMapInRAM();
            return created(jsonObject);
        } catch (Exception e) {
            JsonNode jsonError = ApiError.initError(ApiError.EErrorKey.UnexpectedError, "Ocurrió un error inesperado durante la creación, el servicio no fue creado.");
            apiLogger.error("[create] Ocurrió un error inesperado durante la creación, el servicio no fue creado." + jsonError, e);
            return internalServerError(jsonError);
        } finally {
            debModel.getServer().endTransaction();
        }
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result update(Long id) {
        // Get the form from the request
        Form<models.Service> form = Form.form(models.Service.class).bindFromRequest();
        // Validate errors
        if (form.hasErrors()) {
            JsonNode validationErrors = ApiError.rebuildErrors(form.errors(), models.Service.class.getSimpleName());
            JsonNode jsonError = ApiError.initError(ApiError.EErrorKey.ValidationError, validationErrors.toString());
            apiLogger.error("Service.update: " + jsonError);
            return badRequest(jsonError);
        }

        // Get the object from the form
        models.Service service = form.get();

        // Create the object in db
        debModel.getServer().beginTransaction();
        try {
            models.Service oldService = models.Service.findByProperty("id", id);
            service.setName(oldService.getName());
            models.Service.update(service);
            JsonNode jsonObject = Serializer.serialize(service, serializationFields);
            debModel.getServer().commitTransaction();
            Services.loadMapInRAM();
            return ok(jsonObject);
        } catch (Exception e) {
            JsonNode jsonError = ApiError.initError(ApiError.EErrorKey.UnexpectedError, "Ocurrió un error inesperado durante la actualización, el servicio no fue actualizado.");
            apiLogger.error("Service.update: " + jsonError, e);
            return internalServerError(jsonError);
        } finally {
            debModel.getServer().endTransaction();
        }
    }

    public static Result delete(Long id) {
        models.Service service;

        // Find the object
        try {
            service = models.Service.findByProperty("id", id);
        } catch (Exception e) {
            JsonNode jsonError = ApiError.initError(ApiError.EErrorKey.NotFound, "El servicio que se quiere eliminar no existe." + e);
            return notFound(jsonError);
        }
        // Delete the object from db
        debModel.getServer().beginTransaction();
        try {
            models.Service.delete(service);
            debModel.getServer().commitTransaction();
            Services.loadMapInRAM();
            return ok();
        } catch (Exception e) {
            JsonNode jsonError = ApiError.initError(ApiError.EErrorKey.UnexpectedError, "Ocurrió un error inesperado durante la eliminación, el servicio no fue eliminado.");
            apiLogger.error("Service.delete: " + jsonError, e);
            return internalServerError(jsonError);
        } finally {
            debModel.getServer().endTransaction();
        }
    }

    public static Result serviceParameters(Long serviceId) {
        try {
            models.Service service = models.Service.findByProperty("id", serviceId);
            List<models.Attribute> attributes = service.getAttributes();
            List<String> attributeSerializationFields = Arrays.asList("id", "attribute", "attributeValue");
            JsonNode serialized = Serializer.serialize(attributes, attributeSerializationFields);
            return ok(serialized);
        } catch (Exception e) {
            apiLogger.error("Ocurrió un error inesperado al obtener los parámetros del service", e);
            return internalServerError();
        }
    }

    public static Result findByName(String name) {
        try {
            models.Service service = models.Service.findByProperty("name", name);
            List<models.Attribute> attributes = service.getAttributes();
            List<String> attributeSerializationFields = Arrays.asList("id", "attribute", "attributeValue");
            JsonNode serialized = Serializer.serialize(attributes, attributeSerializationFields);
            return ok(serialized);
        } catch (Exception e) {
            apiLogger.error("Ocurrió un error inesperado al obtener el service", e);
            return internalServerError();
        }
    }

    public static Result createCustomerType() {
        try {
            // Get the form from the request
            Form<models.Attribute> form = Form.form(models.Attribute.class).bindFromRequest();
            // Validate errors
            if (form.hasErrors()) {
                JsonNode validationErrors = ApiError.rebuildErrors(form.errors(), models.Service.class.getSimpleName());
                JsonNode jsonError = ApiError.initError(ApiError.EErrorKey.ValidationError, validationErrors.toString());
                apiLogger.error("Service.createCustomerType: " + jsonError);
                return badRequest(jsonError);
            }

            // Get the object from the form
            models.Attribute attribute = form.get();

            debModel.getServer().beginTransaction();

            models.Service service = models.Service.findByProperty("name", "customerTypes");
            attribute.setService(service);
            models.Attribute.create(attribute);
            Services.loadMapInRAM();

            debModel.getServer().commitTransaction();

            return ok();
        } catch (Exception e) {
            apiLogger.error("Ocurrió un error inesperado al crear el customerType", e);
            return internalServerError();
        } finally {
            debModel.getServer().endTransaction();
        }
    }

    public static Result deleteCustomerType(Long id) {
        models.Attribute attribute = models.Attribute.findByProperty("id", id);

        if (attribute == null) {
            JsonNode jsonError = ApiError.initError(ApiError.EErrorKey.NotFound, "El customer type no existe.");
            return notFound(jsonError);
        }

        // Delete the object from db
        debModel.getServer().beginTransaction();
        try {
            models.Attribute.delete(attribute);
            debModel.getServer().commitTransaction();
            Services.loadMapInRAM();
            return ok();
        } catch (Exception e) {
            JsonNode jsonError = ApiError.initError(ApiError.EErrorKey.UnexpectedError, "Ocurrió un error inesperado durante el borrado del customer type.");
            apiLogger.error(Messages.get("Error.api.list.internalServerError"), e);
            return internalServerError(jsonError);
        } finally {
            debModel.getServer().endTransaction();
        }
    }

}