package dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import helpers.SudamerisResponse;
import play.Logger;

public class ChequeraPendienteRetiroDTO extends SudamerisServiceDTO {
    private Boolean chequeraPendiente;

    public ChequeraPendienteRetiroDTO() {

    }

    @Override
    public void mapToCustomer(SudamerisResponse sudamerisResponse) {
        sudamerisResponse.mapChequeraPendienteRetiro(this);
    }


    public static ChequeraPendienteRetiroDTO mapFromServiceResponse(JsonNode body) {
        ChequeraPendienteRetiroDTO chequeraPendienteRetiroDTO = new ChequeraPendienteRetiroDTO();

        try {
            ArrayNode columns = (ArrayNode) body.get("Result").get("Consultas").get("RepCons.Consulta").get(0).get("Columnas").get("RepCols.Columna");

            for (JsonNode column : columns) {
                String description = column.get("Descripcion").asText();
                int chequerasPend = column.get("Filas").get("RepFilas.Fila").size();
                if ("SCCTA".equals(description)) {
                    chequeraPendienteRetiroDTO.setChequeraPendiente(chequerasPend > 0);
                }
            }
        } catch (Exception e) {
            Logger.of("connector.controllers.application").error("ChequeraPendienteRetiroDTO Error parseando la respuesta del servicio. Body: " + body.asText());
        }

        return chequeraPendienteRetiroDTO;
    }

    public Boolean getChequeraPendiente() {
        return chequeraPendiente;
    }

    public void setChequeraPendiente(Boolean chequeraPendiente) {
        this.chequeraPendiente = chequeraPendiente;
    }
}
