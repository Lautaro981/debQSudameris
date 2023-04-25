package helpers;

import com.google.common.collect.Multimap;
import dto.ChequeraPendienteRetiroDTO;
import dto.Customer;
import dto.Error;
import dto.SegmentoPersonaDTO;
import dto.TarjetaPendienteEntregaDTO;

import java.util.Map;

public class SudamerisResponse {
    private Customer customer;

    public SudamerisResponse() {
        this.customer = new Customer();
    }

    public void mapSegmentoPersona(SegmentoPersonaDTO segmentoPersonaDTO) {
        this.customer.setFirstName(segmentoPersonaDTO.getApenom());
        this.customer.setCustomerType(segmentoPersonaDTO.getSegment());
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void mapChequeraPendienteRetiro(ChequeraPendienteRetiroDTO chequeraPendienteRetiroDTO) {
        Boolean chequeraPendiente = chequeraPendienteRetiroDTO.getChequeraPendiente();
        if (chequeraPendiente != null) {
            String value = chequeraPendiente ? "Sí" : "No";
            this.customer.addExtraField("workstation", "onlyValue", "Chequera Pendiente de Retiro", value);
        }
    }

    public void mapTarjetaPendienteEntrega(TarjetaPendienteEntregaDTO tarjetaPendienteEntregaDTO) {
        Map<String, Boolean> cards = tarjetaPendienteEntregaDTO.getCards();
        cards.forEach((key, value) -> {
            String result = value ? "Si" : "No";
            String keyValue = adaptKeyString(key);
            this.customer.addExtraField("workstation", "onlyValue", keyValue, result);
        });

        String account = tarjetaPendienteEntregaDTO.getAccount();
        if (account != null)
            this.customer.addExtraField("workstation", "onlyValue", "Cuenta", account);

        String denomination = tarjetaPendienteEntregaDTO.getDenomination();
        if (denomination != null)
            this.customer.addExtraField("workstation", "onlyValue", "Denominación", denomination);
    }

    private String adaptKeyString(String key) {
        key = key.toLowerCase();
        key = key.substring(0, 1).toUpperCase() + key.substring(1);
        String name = key + " pendiente de retiro";
        return name;
    }
}
