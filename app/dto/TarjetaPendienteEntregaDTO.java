package dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import helpers.SudamerisResponse;
import play.Logger;

import java.util.HashMap;
import java.util.Map;

public class TarjetaPendienteEntregaDTO extends SudamerisServiceDTO {
    private static Map<String, Boolean> cards;
    private String account;
    private String denomination;


    public TarjetaPendienteEntregaDTO() {
        super();
    }

    @Override
    public void mapToCustomer(SudamerisResponse sudamerisResponse) {
        sudamerisResponse.mapTarjetaPendienteEntrega(this);
    }

    public static TarjetaPendienteEntregaDTO mapFromServiceResponse(JsonNode body) {
        TarjetaPendienteEntregaDTO tarjetaPendienteEntregaDTO = new TarjetaPendienteEntregaDTO();
        try {
            tarjetaPendienteEntregaDTO.setCards(new HashMap());

            ArrayNode columns = (ArrayNode) body.get("Result").get("Consultas").get("RepCons.Consulta").get(0).get("Columnas").get("RepCols.Columna");

            ObjectNode instance = null;
            ObjectNode products = null;
            ObjectNode cardsNumbers = null;
            String account = null;
            String denomination = null;

            for (JsonNode column : columns) {
                String description = column.get("Descripcion").asText();
                if ("INSTANCIA".equals(description)) {
                    instance = (ObjectNode) column;
                } else if ("PRODUCTO".equals(description)) {
                    products = (ObjectNode) column;
                }
                else if ("NROTARJETA".equals(description)){
                    cardsNumbers = (ObjectNode) column;
                }
                else if ("CUENTA".equals(description)) {
                    try {
                        JsonNode value = column.get("Filas").get("RepFilas.Fila");
                        if (value.size() > 0) {
                            account = value.get(0).get("Valor").asText();
                        }
                    } catch (Exception e) {
                        Logger.of("connector.controllers.application").error("TarjetaPendienteEntregaDTO El campo " + description + " no tiene valor");
                    }
                    tarjetaPendienteEntregaDTO.setAccount(account);
                } else if ("Denominacion".equals(description)) {
                    try {
                        JsonNode filas = column.get("Filas").get("RepFilas.Fila");
                        if (filas.size() > 0) {
                            denomination = filas.get(0).get("Valor").asText();
                        }
                    } catch (Exception e) {
                        Logger.of("connector.controllers.application").error("TarjetaPendienteEntregaDTO El campo " + description + " no tiene valor");
                    }
                    tarjetaPendienteEntregaDTO.setDenomination(denomination);
                }


                if (instance != null && products != null && account != null && denomination != null && cardsNumbers != null)
                    break;
            }

            try {
                for (JsonNode row : products.get("Filas").get("RepFilas.Fila")) {
                    String number = row.get("Numero").asText();
                    Boolean result = numberIsPresentInRows(number, instance);
                    
                    String cardNumber = getCardNumber(number, cardsNumbers);

                    cards.put(row.get("Valor").asText() + " " + cardNumber, result);
                }
            } catch (Exception e) {
                Logger.of("connector.controllers.application").error("TarjetaPendienteEntregaDTO Error intentando leer las filas de tarjetas: " + e);
            }
        } catch (Exception e) {
            Logger.of("application").error("TarjetaPendienteEntregaDTO Error parseando la respuesta del servicio. Body: " + body.asText());
        }

        return tarjetaPendienteEntregaDTO;
    }

    private static String getCardNumber(String number, ObjectNode cardsNumbers) {
        String cardNumber = "";
        for (JsonNode row : cardsNumbers.get("Filas").get("RepFilas.Fila")) {
            if (row.get("Numero").asText().equals(number)) {
                cardNumber = row.get("Valor").asText();
                break;
            }
        }
        return cardNumber;
    }

    private static Boolean numberIsPresentInRows(String number, ObjectNode products) {
        Boolean result = false;

        for (JsonNode row : products.get("Filas").get("RepFilas.Fila")) {
            if (row.get("Numero").asText().equals(number)) {
                result = true;
                break;
            }
        }

        return result;
    }

    public Map<String, Boolean> getCards() {
        return cards;
    }

    public void setCards(Map<String, Boolean> cards) {
        TarjetaPendienteEntregaDTO.cards = cards;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getDenomination() {
        return denomination;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }
}
