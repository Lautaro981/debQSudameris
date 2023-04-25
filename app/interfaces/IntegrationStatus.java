package interfaces;

import utils.StatusResponse;
import play.libs.F;

public interface IntegrationStatus {

    F.Promise<StatusResponse> getIntegrationStatus();
}
