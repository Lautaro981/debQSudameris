package utils;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.Instant;

public class StatusResponse {
    private Status status;
    private String nameIntegration;
    private Instant updatedAt;

    public StatusResponse(String nameIntegration, Status status) {
        this.status = status;
        this.nameIntegration = nameIntegration;
        this.updatedAt = Instant.now();
    }

    public Status getStatus() {
        return status;
    }

    public String getNameIntegration() {
        return nameIntegration;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public ObjectNode toJsonNode() {
        return JsonNodeFactory.instance.objectNode()
                .put("name", this.getNameIntegration())
                .put("status", this.getStatus().toString())
                .put("updated_at", this.getUpdatedAt().toString());
    }
}
