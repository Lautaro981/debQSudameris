package models;

import com.debmedia.utils.controllers.WS.JsonBody;
import com.debmedia.utils.controllers.WS.PlayWSResponseWrapper;
import com.debmedia.utils.models.debModel;
import org.joda.time.LocalDate;
import play.Logger;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "token")
public class Token extends debModel {

    public interface creating {
    }

    //          ATTRIBUTES
    @Id
    private Long id;

    private String value;

    private LocalDate lastUpdate;

    private static final Finder<Long, Token> finder = (Finder<Long, Token>) debModel.getFinder(Token.class);

    public static Token get() {
        return finder.findUnique();
    }

    public boolean isFromToday() {
        return this.lastUpdate.equals(LocalDate.now());
    }

    public static void updateValue(PlayWSResponseWrapper response) {
        JsonBody body = (JsonBody) response.getBody();
        String token = body.getBody().get("SessionToken").asText();
        Token oldToken = get();
        oldToken.setValue(token);
        oldToken.setLastUpdate(LocalDate.now());
        try{
            debModel.getServer().beginTransaction();
            oldToken.update();
            debModel.getServer().commitTransaction();
            Logger.of("models.Token").debug("Token actualizado en la DB.");
        }
        catch (Exception e){
            debModel.getServer().rollbackTransaction();
            Logger.of("models.Token").error("Error intentando actualizar el token en la DB.", e);
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public LocalDate getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDate lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return "Token{" +
                "id=" + id +
                ", value='" + value + '\'' +
                ", lastUpdate=" + lastUpdate +
                '}';
    }
}
