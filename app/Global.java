/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import akka.actor.Cancellable;
import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import exceptions.ApiError;
import org.slf4j.MDC;
import play.Application;
import play.Configuration;
import play.GlobalSettings;
import play.data.format.Formatters;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Results;
import play.mvc.SimpleResult;

import java.io.File;
import java.lang.reflect.Method;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

import static play.libs.Crypto.decryptAES;

/**
 * @author ejesposito
 */
public class Global extends GlobalSettings {

    private Cancellable scheduler;

    @Override
    public void onStart(Application app) {
        controllers.Services.loadMapInRAM();
        Formatters.register(Time.class, new Formatters.SimpleFormatter<Time>() {
            @Override
            public Time parse(String input, Locale l) throws ParseException {
                DateFormat df = new SimpleDateFormat("HH:mm");
                return new Time(df.parse(input).getTime());
            }

            @Override
            public String print(Time localTime, Locale l) {
                return localTime.toString();
            }
        });
    }

    @Override
    public Configuration onLoadConfig(Configuration configuration, File file, ClassLoader classLoader) {
        if (configuration.getString("application.mode").equals("production")) {
            HashMap<String, String> configMap = new HashMap<>();
            String ebeanServerName = configuration.getString("ebeanServerName");
            String dbProperty = "db." + ebeanServerName;
            String isEncrypted = configuration.getString(dbProperty + ".isEncrypted");
            if (isEncrypted.equals("true")) {
                String encryptedPassword = configuration.getString(dbProperty + ".password");
                String secret = configuration.getString("application.secret");
                String decryptedPassword = decryptAES(encryptedPassword, secret.substring(0, 16));
                configMap.put(dbProperty + ".password", decryptedPassword);
                Config config = ConfigFactory.parseMap(configMap);
                return new Configuration(config.withFallback(configuration.getWrappedConfiguration().underlying()));
            } else {
                return super.onLoadConfig(configuration, file, classLoader);
            }
        } else if (configuration.getString("application.mode").equals("install")) {
            // En caso de aplicación en modo instalación se carga el archivo aplication.install.conf sin dbs
            String installConfigFile = "application.install.conf";
            return new play.Configuration(ConfigFactory.load(installConfigFile));
        } else {
            return super.onLoadConfig(configuration, file, classLoader);
        }
    }

    @Override
    public F.Promise<SimpleResult> onHandlerNotFound(Http.RequestHeader request) {
        // Return corporative page if html is suported by client
        if (request.accepts("text/html")) {
            return F.Promise.<SimpleResult>pure(Results.notFound(views.html.error.notFound.render(request.method(), request.uri())));
        } else {
            // Return a mesaje in json format
            return F.Promise.<SimpleResult>pure(Results.notFound(ApiError.initError(ApiError.EErrorKey.InvalidRoute)));
        }
    }

    @Override
    public Action onRequest(Http.Request request, Method actionMethod) {
        return new Action.Simple() {
            @Override
            public F.Promise<SimpleResult> call(Http.Context ctx) throws Throwable {

                if (ctx.request().body() != null) {
                    JsonNode body = ctx.request().body().asJson();
                    //Cargo el MDC en el thread
                    if (body != null && body.has("code")) {
                        MDC.put("code", body.get("code").asText());
                    }

                    F.Promise<SimpleResult> result = delegate.call(ctx);

                    //Libero el MDC del thread
                    MDC.remove("code");

                    return result;
                } else {
                    return delegate.call(ctx);
                }
            }
        };
    }


}
