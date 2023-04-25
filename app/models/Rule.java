package models;

import com.debmedia.utils.models.debModel;
import play.Logger;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "map_rule")
public class Rule extends debModel {


    public interface creating {
    }

    public interface updating {
    }

    //          ATTRIBUTES
    @Id
    private Long id;

    private String segment;

    @ManyToOne
    @JoinColumn(name = "q_customer_type_id")
    private QCustomerType qCustomerType;

    //          DB OPERATIONS
    private static final Finder<Long, Rule> finder = (Finder<Long, Rule>) debModel.getFinder(Rule.class);

    public static Rule findBySegment(String segment) {
        return finder.where().eq("segment", segment).findUnique();
    }

    public void create() {
        try{
            debModel.getServer().beginTransaction();
            this.save();
            debModel.getServer().commitTransaction();
        }
        catch (Exception e){
            Logger.of("models.Rule").error("Falló la creacion de la regla de mapeo de segmento.", e);
            debModel.getServer().rollbackTransaction();
        }
        finally {
            debModel.getServer().endTransaction();
        }
    }

    public static List<Rule> getList() {
        return finder.all();
    }

    public static Rule findById(Long id) {
        return finder.byId(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QCustomerType getqCustomerType() {
        return qCustomerType;
    }

    public void setqCustomerType(QCustomerType qCustomerType) {
        this.qCustomerType = qCustomerType;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }
}
