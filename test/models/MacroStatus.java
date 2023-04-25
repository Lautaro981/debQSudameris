package models;

import interfaces.IntegrationStatus;
import play.libs.F;
import utils.Status;
import utils.StatusResponse;

import static java.lang.Thread.sleep;

public class MacroStatus implements IntegrationStatus {
    @Override
    public F.Promise<StatusResponse> getIntegrationStatus() {
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return F.Promise.<StatusResponse>pure(new StatusResponse("Macro", Status.OK));
    }
}
