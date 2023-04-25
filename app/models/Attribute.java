package models;

import com.debmedia.utils.models.debModel;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "attribute")
public class Attribute extends debModel {

    //          ATTRIBUTES
    @Id
    private Long id;

    private String attribute;

    // String attributeValue (crear con tipo de dato varchar(max) en la base de datos para permitir más de 256 caracteres)
    @Column
    @Lob
    private String attributeValue;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    //          DB OPERATIONS
    private static final Finder<Long, Attribute> finder = (Finder<Long, Attribute>) debModel.getFinder(Attribute.class);

    /**
     * This method search in the database all the cAttributes.
     *
     * @return List<Attribute>
     * @throws Exception
     */
    public static List<Attribute> getList() throws Exception {
        return finder.all();
    }

    /**
     * This method create a Attribute in database.
     *
     * @param attribute
     * @throws Exception
     */
    public static void create(Attribute attribute) throws Exception {
        attribute.save();
    }

    /**
     * This method search in the database looking for an unique Attribute using the attribute and the
     * corresponding object given by the parameters.
     *
     * @param key
     * @param obj
     * @return Attribute
     * @throws Exception
     */
    public static Attribute findByProperty(String key, Object obj) {
        return finder.where().eq(key, obj).findUnique();
    }

    /**
     * This method search in the database looking for all cAttributes usvoiding the attribute and the
     * corresponding object given by the parameters.
     *
     * @param key
     * @param obj
     * @return List<Attribute>
     * @throws Exception
     */
    public static List<Attribute> findAllByProperty(String key, Object obj) throws Exception {
        return finder.where().eq(key, obj).findList();
    }

    /**
     * This method update a Attribute.
     *
     * @param attribute
     * @throws Exception
     */
    public static void update(Attribute attribute) throws Exception {
        attribute.update();
    }

    /**
     * This method delete a Attribute
     *
     * @param attribute
     * @throws Exception
     */
    public static void delete(Attribute attribute) throws Exception {
        attribute.delete();
    }

    //          GET/SET
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
