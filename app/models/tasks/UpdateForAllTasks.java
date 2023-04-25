package models.tasks;

import com.debmedia.utils.models.Server;
import com.debmedia.utils.models.ServerTask;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import play.Logger;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("UpdateForAll")
public class UpdateForAllTasks extends ServerTask {

    private final static Logger.ALogger logger = Logger.of("connector.models.tasks.updateforalltasks");

    public UpdateForAllTasks(Boolean oneShot, int periodInSeconds, Boolean visible) {
        super("CONECTORBINGOAGG", false, null, oneShot, Timestamp.from(Instant.now()), periodInSeconds, null, null, visible, "", "");
    }

    @Override
    public void run() {

        //Por defecto el tiempo es 10 minutos
        Long minutesBeforeOffline = 10L;
        try {
            minutesBeforeOffline = ConfigFactory.load().getLong("server.beforeOffline");
        } catch (ConfigException.Missing e) {
            logger.info("Debe agregar la configuración de server.beforeOffline, se usa por defecto 10 (minutos)");
        }

        //Uso el tiempo actual para revisar cuanto tiempo estuvo desconectado
        LocalDateTime now = LocalDateTime.now();

        //Solo necesito revisar los servidores online a este momento
        List<Server> allServers = Server.findAllOnline();

        for (Server server : allServers) {
            if (server.getLastActivity() != null) {
                LocalDateTime ts = LocalDateTime.ofInstant(server.getLastActivity().toInstant(), ZoneId.systemDefault());

                //Si el tiempo de actualización del servidor es más largo que el minutesBeforeOffline => lo seteo offline
                //Y además le saco las tareas asignadas
                if (ts.until(now, ChronoUnit.MINUTES) >= minutesBeforeOffline) {
                    server.setOnline(false);
                    server.setTasks(new ArrayList<>());
                    Server.update(server);

                }
            }
        }
    }
}
