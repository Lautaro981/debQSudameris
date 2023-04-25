package annotations;

import com.fasterxml.jackson.databind.JsonNode;
import exceptions.ApiError;
import play.Logger;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.SimpleResult;


public class bErrorCatcherAction extends Action<bErrorCatcher> {

    private final static Logger.ALogger logger = Logger.of("connector.annotations.bErrorCatcherAction");

    @Override
    public F.Promise<SimpleResult> call(Http.Context cntxt) throws Throwable {
        try {
            return delegate.call(cntxt);
        } catch (final Throwable e) {
            return F.Promise.promise(new F.Function0<SimpleResult>() {
                @Override
                public SimpleResult apply() {
                    JsonNode response = ApiError.initError(ApiError.EErrorKey.UnexpectedError, "Ocurrió un error inesperado");
                    logger.error("Error: ", e);
                    return internalServerError(response);
                }
            });
        }
    }
}
