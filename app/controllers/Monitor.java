package controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import utils.StatusResponse;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import utils.LoaderMonitor;

import java.util.ArrayList;

public class Monitor extends Controller {
    public static F.Promise<Result> getMonitorsStatus() {
        LoaderMonitor monitor = LoaderMonitor.getInstance();
        ArrayList<F.Promise<StatusResponse>> promises = new ArrayList<>();
        monitor.getMonitors().forEach(integrationStatus -> {
            promises.add(integrationStatus.getIntegrationStatus());
        });

        return F.Promise.sequence(new ArrayList<>(promises)).map(statusResponses -> {
            ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
            for (StatusResponse statusResponse : statusResponses) {
                arrayNode.add(statusResponse.toJsonNode());
            }
            return ok(arrayNode);
        });
    }
}
