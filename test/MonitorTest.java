import com.fasterxml.jackson.databind.JsonNode;
import controllers.Monitor;
import models.MacroStatus;
import models.WhylineStatus;
import org.junit.Before;
import org.junit.Test;
import play.libs.Json;
import play.mvc.Result;
import utils.LoaderMonitor;
import utils.Status;
import utils.StatusResponse;

import java.util.concurrent.TimeUnit;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.contentAsString;

public class MonitorTest {

    @Before
    public void clearLoader() {
        LoaderMonitor.getInstance().clearMonitors();
    }

    @Test
    public void StatusResponseTest() {
        StatusResponse response = new StatusResponse("Macro", Status.DISABLED);
        JsonNode json = response.toJsonNode();
        assertThat(json.get("name").asText()).isEqualTo("Macro");
        assertThat(json.get("status").asText()).isEqualTo(Status.DISABLED.toString());
    }

    @Test
    public void LoaderMonitorTest() {
        LoaderMonitor loader = LoaderMonitor.getInstance();
        LoaderMonitor secondLoader = LoaderMonitor.getInstance();

        loader.addMonitor(new MacroStatus()).addMonitor(new WhylineStatus());

        assertThat(secondLoader).isEqualTo(loader);
        assertThat(loader.getMonitors().size()).isEqualTo(2);
        assertThat(secondLoader.getMonitors().size()).isEqualTo(2);
    }

    @Test
    public void MonitorResponseTest() {
        LoaderMonitor loader = LoaderMonitor.getInstance();
        loader.addMonitor(new MacroStatus()).addMonitor(new WhylineStatus());

        Result result = Monitor.getMonitorsStatus().get(5, TimeUnit.SECONDS);
        String body = contentAsString(result);

        JsonNode node = Json.parse(body);
        node.elements().forEachRemaining(jsonNode -> {
            String name = jsonNode.get("name").asText();
            assertThat(name.equals("Macro") || name.equals("Whyline")).isTrue();

            String status = jsonNode.get("status").asText();
            assertThat(status.equals("OK") || status.equals("DISABLED")).isTrue();
        });
    }
}
