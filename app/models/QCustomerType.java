package models;

import com.avaje.ebean.ExpressionList;
import com.debmedia.utils.models.debModel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "q_customer_type")
public class QCustomerType extends debModel {
    @Id
    private Long id;

    private String label;

    private Long id_q;

    private static final Finder<Long, QCustomerType> finder = (Finder<Long, QCustomerType>) debModel.getFinder(QCustomerType.class);

    public interface creating {
    }

    public interface updating {
    }

    public static QCustomerType findByLabel(String label) {
        ExpressionList<QCustomerType> expList = finder.where();

        expList.eq("label", label);

        return expList.findUnique();
    }

    public static List<QCustomerType> getList() {
        return finder.all();
    }

    public static QCustomerType findById(Long id) {
        ExpressionList<QCustomerType> expList = finder.where();

        expList.eq("id", id);

        return expList.findUnique();
    }

    public static QCustomerType getDefault() {
        return findByLabel("No cliente");
    }

    public static void delete(QCustomerType customerType) {
        try{
            debModel.getServer().beginTransaction();
            customerType.delete();
            debModel.getServer().commitTransaction();
        }
        catch (Exception e){
            debModel.getServer().rollbackTransaction();
            throw e;
        }
        finally {
            debModel.getServer().endTransaction();
        }

    }

    public static void update(QCustomerType customerType) {
        if (customerType.getId_q() == null) {
            QCustomerType ctDefault = findByLabel("No cliente");
            customerType.setId_q(ctDefault.getId_q());
        }
        if (customerType.getLabel() == null) customerType.setLabel("No cliente");

        try{
            debModel.getServer().beginTransaction();
            customerType.update();
            debModel.getServer().commitTransaction();
        }
        catch (Exception e){
            debModel.getServer().rollbackTransaction();
            throw e;
        }
        finally {
            debModel.getServer().endTransaction();
        }
    }

    public static void create(QCustomerType customerType) {
        try{
            debModel.getServer().beginTransaction();
            customerType.save();
            debModel.getServer().commitTransaction();
        }
        catch (Exception e){
            debModel.getServer().rollbackTransaction();
            throw e;
        }
        finally {
            debModel.getServer().endTransaction();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getId_q() {
        return id_q;
    }

    public void setId_q(Long id_q) {
        this.id_q = id_q;
    }
}
