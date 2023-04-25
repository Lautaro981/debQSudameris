package models;

import com.debmedia.utils.models.debModel;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static play.libs.Crypto.encryptAES;

@Entity
@Table(name = "service")
public class Service extends debModel {

    public interface creating {
    }

    //          ATTRIBUTES
    @Id
    private Long id;

    @Constraints.Required(groups = creating.class)
    private String name;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    private List<Attribute> attributes;

    //          DB OPERATIONS
    private static final Finder<Long, Service> finder = (Finder<Long, Service>) debModel.getFinder(Service.class);

    /**
     * This method search in the database all the cServices.
     *
     * @return List<Service>
     * @throws Exception
     */
    public static List<Service> getList() {
        return finder.all();
    }

    /**
     * This method create a Service in database.
     *
     * @param service
     * @throws Exception
     */
    public static void create(Service service) throws Exception {
        encryptPassword(service);
        service.save();
    }

    /**
     * This method search in the database looking for an unique Service using the key and the corresponding
     * object given by the parameters.
     *
     * @param key
     * @param obj
     * @return Service
     * @throws Exception
     */
    public static Service findByProperty(String key, Object obj) throws Exception {
        return finder.where().eq(key, obj).findUnique();
    }

    /**
     * This method search in the database looking for all cServices using the key and the corresponding object
     * given by the parameters.
     *
     * @param key
     * @param obj
     * @return List<Service>
     * @throws Exception
     */
    public static List<Service> findAllByProperty(String key, Object obj) throws Exception {
        return finder.where().eq(key, obj).findList();
    }

    /**
     * Creating objects by hand(new), called dirty, allow you to remove elements in the relation, if a list is
     * null, it will assign a new("dirty") empty list to his value, then, the the relation will be broken by
     * the cascade annotation, and will to remove the unreferenced objects.
     */
    private void assignDirtyOnNullBidirectionals() {
        if (this.getAttributes() == null) {
            this.setAttributes(new ArrayList());
        }
    }

    private static void encryptPassword(Service service) throws Exception {
        for (int i = 0; i < service.getAttributes().size(); i++) {
            if ("password".equalsIgnoreCase(service.getAttributes().get(i).getAttribute())) {
                String oldPassword = Attribute.findByProperty("attribute", "password").getAttributeValue();

                if (!service.getAttributes().get(i).getAttributeValue().equalsIgnoreCase(oldPassword)) {
                    service.getAttributes().get(i).setAttributeValue(encryptAES(service.getAttributes().get(i).getAttributeValue()));
                }
            }
        }
    }

    /**
     * This method update a Service.
     *
     * @param service
     * @throws Exception
     */
    public static void update(Service service) throws Exception {
        encryptPassword(service);
        service.assignDirtyOnNullBidirectionals();
        service.update();
    }

    /**
     * This method delete a Service
     *
     * @param service
     * @throws Exception
     */
    public static void delete(Service service) throws Exception {
        service.delete();
    }

    //          GET/SET
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

}
