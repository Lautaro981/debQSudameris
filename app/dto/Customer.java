package dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.QCustomerType;
import models.Rule;
import play.Logger;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Customer {

    private final static Logger.ALogger logger = Logger.of("connector.dto.customer");

    private String firstName;
    private String cedulaIdentidad;
    private String dni;
    private CustomerType customerType;
    private ArrayNode customerExtraFields;

    public Customer() {
        this.customerExtraFields = JsonNodeFactory.instance.arrayNode();
    }

    public Customer(String doc, String cedulaIdentidad) {
        this.setDni(doc);
        this.setCedulaIdentidad(cedulaIdentidad);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getDni() {
        return dni;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public ArrayNode getCustomerExtraFields() {
        return customerExtraFields;
    }

    public void setCustomerExtraFields(ArrayNode customerExtraFields) {
        this.customerExtraFields = customerExtraFields;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }

    public String getCedulaIdentidad() {
        return cedulaIdentidad;
    }

    public void setCedulaIdentidad(String cedulaIdentidad) {
        this.cedulaIdentidad = cedulaIdentidad;
    }

    public void setCustomerType(String segmento) {
        Rule rule = Rule.findBySegment(segmento);

        CustomerType customerType;

        if (rule == null) {
            customerType = new CustomerType(QCustomerType.getDefault().getId_q());
        } else {
            customerType = new CustomerType(rule.getqCustomerType().getId_q());
        }

        this.customerType = customerType;
    }

    public void setCustomerTypeDefault() {
        CustomerType customerType = new CustomerType(QCustomerType.getDefault().getId_q());
        this.customerType = customerType;
    }

    public void addExtraField(String in, String format, String key, String value) {
        ObjectNode extraField = JsonNodeFactory.instance.objectNode();
        ArrayNode showable = JsonNodeFactory.instance.arrayNode();
        ObjectNode workstation = JsonNodeFactory.instance.objectNode();

        workstation.put("in", in);
        workstation.put("format", format);
        showable.add(workstation);

        extraField.put(key, value);
        extraField.put("showable", showable);

        this.customerExtraFields.add(extraField);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "firstName='" + firstName + '\'' +
                ", dni='" + dni + '\'' +
                ", customerType=" + customerType +
                ", customerExtraFields=" + customerExtraFields +
                '}';
    }
}
