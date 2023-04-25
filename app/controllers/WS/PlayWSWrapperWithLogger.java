package controllers.WS;

import com.debmedia.utils.controllers.WS.GenericBody;
import com.debmedia.utils.controllers.WS.PlayWSResponseWrapper;
import com.debmedia.utils.controllers.WS.PlayWSWrapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.MDC;
import play.Logger;
import play.libs.F;

import java.util.Map;

public class PlayWSWrapperWithLogger extends PlayWSWrapper {

    private static final Logger.ALogger logger = Logger.of("connector.controllers.WS.PlayWSWrapperWithLogger");

    public PlayWSWrapperWithLogger(com.debmedia.utils.controllers.WS.PlayWSWrapper.HttpVerb httpVerb, String url, Map<String, String> headers, GenericBody body) {

        super(httpVerb, url, headers, body);
    }

    public F.Promise<PlayWSResponseWrapper> execute(Long timeout) {
        Map<String, String> mdcOld = MDC.getCopyOfContextMap();
        Long startTime = System.currentTimeMillis();
        F.Promise<PlayWSResponseWrapper> execute = super.execute(timeout);
        return execute.map(
                new F.Function<PlayWSResponseWrapper, PlayWSResponseWrapper>() {
                    @Override
                    public PlayWSResponseWrapper apply(PlayWSResponseWrapper playWSResponseWrapper) throws Throwable {
                        logger.debug("Response." + playWSResponseWrapper.toString());
                        if (logger.isDebugEnabled()) {
                            MDC.setContextMap(mdcOld);
                            Long elapsedTime = System.currentTimeMillis() - startTime;
                            String responseBody = playWSResponseWrapper.getBody().getBodyAsString();
                            ObjectNode log = JsonNodeFactory.instance.objectNode();
                            log.put("url", getUrl());
                            log.put("status", playWSResponseWrapper.getStatus());
                            log.put("requestBody", getBody().getBodyAsString());
                            log.put("responseBody", responseBody);
                            log.put("latency", elapsedTime);
                            logger.debug(log.toString());
                            MDC.clear();
                        }
                        return playWSResponseWrapper;
                    }
                });
    }
}
