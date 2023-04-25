package dto;

import com.fasterxml.jackson.databind.JsonNode;
import helpers.SudamerisResponse;

public abstract class SudamerisServiceDTO {

    public SudamerisServiceDTO() {

    }

    public abstract void mapToCustomer(SudamerisResponse sudamerisResponse);
}
