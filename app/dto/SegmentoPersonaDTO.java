package dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import helpers.SudamerisResponse;
import play.Logger;

public class SegmentoPersonaDTO extends SudamerisServiceDTO {
    private String apenom;
    private String segment;

    public SegmentoPersonaDTO() {
        super();
    }

    @Override
    public void mapToCustomer(SudamerisResponse sudamerisResponse) {
        sudamerisResponse.mapSegmentoPersona(this);
    }

    public static SegmentoPersonaDTO mapFromServiceResponse(JsonNode body) {
        SegmentoPersonaDTO segmentoPersonaDTO = new SegmentoPersonaDTO();
        try {
            ArrayNode columns = (ArrayNode) body.get("Result").get("Consultas").get("RepCons.Consulta").get(0).get("Columnas").get("RepCols.Columna");

            for (JsonNode column : columns) {
                String description = column.get("Descripcion").asText();
                String value;
                try {
                    value = column.get("Filas").get("RepFilas.Fila").get(0).get("Valor").asText();
                } catch (Exception e) {
                    Logger.of("connector.controllers.application").error("SegmentoPersonaDTO El campo " + description + " no tiene valor");
                    value = null;
                }

                if ("APENOM".equals(description)) {
                    segmentoPersonaDTO.setApenom(value);
                } else if ("SEGM".equals(description)) {
                    segmentoPersonaDTO.setSegment(value);
                }
            }
        } catch (Exception e) {
            Logger.of("application").error("SegmentoPersonaDTO Error parseando la respuesta del servicio. Body: " + body.asText());
        }

        return segmentoPersonaDTO;
    }

    public String getApenom() {
        return apenom;
    }

    public void setApenom(String apenom) {
        this.apenom = apenom;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }
}
