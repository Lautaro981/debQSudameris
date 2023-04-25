package controllers;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.debmedia.utils.controllers.apiresult.ResultOk;
import com.debmedia.utils.controllers.apiresult.resulterror.BadRequest;
import com.debmedia.utils.controllers.apiresult.resulterror.InternalServerError;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.LoggerFactory;
import play.Logger;
import play.Play;
import play.mvc.Controller;
import play.mvc.Result;
import utils.translations.TranslationKey;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Support extends Controller {

    private static final Logger.ALogger logger = Logger.of("connector.controllers.support");

    public static Result downloadLogs(String zipIncluded) {
        try {
            String path = System.getProperty("user.dir").replaceAll("\\\\", "/");
            File logsPath = new File(getLogsPath());
            File folder = new File(path + "/temp");
            folder.mkdirs();
            File outputFile = new File(path + "/temp/logs.zip");
            FilenameFilter filter = null;

            if (!Boolean.parseBoolean(zipIncluded)) {
                filter = (dir, name) -> !(name.toLowerCase().endsWith(".zip"));
            }

            zipDirectory(logsPath, outputFile, filter);
            return new ResultOk(outputFile).create();
        } catch (Exception e) {
            logger.error("[downloadLogs] Error en la creación de logs.zip", e);
            return new InternalServerError(TranslationKey.CONTROLLERS_SUPPORT_ERROR_ZIP).create();
        }
    }

    private static String getLogsPath() {
        String customLogs = Play.application().configuration().getString("logs.path");
        if (customLogs.equals("logs")) {
            return (System.getProperty("user.dir") + "/logs").replaceAll("\\\\", "/");
        } else {
            return customLogs.replaceAll("\\\\", "/");
        }
    }

    private static void zipDirectory(File dir, File zipFile, FilenameFilter filter) throws IOException {
        FileOutputStream fout = new FileOutputStream(zipFile);
        ZipOutputStream zout = new ZipOutputStream(fout);
        zipSubDirectory("", dir, zout, filter);
        zout.close();
    }

    private static void zipSubDirectory(String basePath, File dir, ZipOutputStream zout, FilenameFilter filter) throws IOException {
        byte[] buffer = new byte[4096];
        File[] files = dir.listFiles(filter);
        for (File file : files) {
            if (file.isDirectory()) {
                String path = basePath + file.getName() + "/";
                zout.putNextEntry(new ZipEntry(path));
                zipSubDirectory(path, file, zout, filter);
                zout.closeEntry();
            } else {
                FileInputStream fin = new FileInputStream(file);
                zout.putNextEntry(new ZipEntry(basePath + file.getName()));
                int length;
                while ((length = fin.read(buffer)) > 0) {
                    zout.write(buffer, 0, length);
                }
                zout.closeEntry();
                fin.close();
            }
        }
    }

    public static Result getLoggerLevel(String loggerName) {
        try {
            if (!findNamesOfConfiguredAppenders().contains(loggerName)) {
                logger.warn("[getLoggerLevel] el nombre del logger es inválido.");
                return new BadRequest(TranslationKey.CONTROLLERS_SUPPORT_INVALID_LOGGER_NAME).create();
            }
            ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(loggerName);
            String loggerLevel = logger.getLevel().toString();
            return new ResultOk(loggerLevel).create();
        } catch (Exception e) {
            logger.error("[getLoggerLevel] Error obteniendo el nivel del log " + loggerName, e);
            return new InternalServerError(TranslationKey.CONTROLLERS_SUPPORT_ERROR_GET_LOG_LEVEL).create();
        }
    }

    public static Result setLoggerLevel(String loggerName, String strLevel) {
        try {
            if (!findNamesOfConfiguredAppenders().contains(loggerName)) {
                logger.warn("[setLoggerLevel] El nombre del logger es inválido ");
                return new BadRequest(TranslationKey.CONTROLLERS_SUPPORT_INVALID_LOGGER_NAME).create();
            }
            ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(loggerName);
            //Map strLevel to level (default value will be the actual value)
            Level level = Level.toLevel(strLevel, logger.getLevel());
            logger.setLevel(level);
            return new ResultOk(logger.getLevel().toString()).create();
        } catch (Exception e) {
            logger.error("[setLoggerLevel] Error seteando el nivel del log " + loggerName, e);
            return new InternalServerError(TranslationKey.CONTROLLERS_SUPPORT_ERROR_SET_LOG_LEVEL).create();
        }
    }

    public static Result getLoggers() {
        try {
            //Arma la lista de loggers con sus levels
            ArrayNode response = JsonNodeFactory.instance.arrayNode();
            for (String name : findNamesOfConfiguredAppenders()) {
                ObjectNode node = JsonNodeFactory.instance.objectNode();
                ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(name);
                Level level = Level.toLevel(logger.getLevel().toString());
                node.put("name", name);
                node.put("level", level.toString());
                response.add(node);
            }
            return new ResultOk(response).create();
        } catch (Exception e) {
            logger.error("[getLoggers] Error obteniendo los logs del logger.xml", e);
            return new InternalServerError(TranslationKey.CONTROLLERS_SUPPORT_ERROR_GET_LOGS).create();
        }
    }

    private static List<String> findNamesOfConfiguredAppenders() {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        List<String> strList = new ArrayList<>();
        for (ch.qos.logback.classic.Logger log : lc.getLoggerList()) {
            if(log.getLevel() != null || hasAppenders(log)) {
                strList.add(log.getName());
            }
        }
        return strList;
    }

    private static boolean hasAppenders(ch.qos.logback.classic.Logger logger) {
        Iterator<Appender<ILoggingEvent>> it = logger.iteratorForAppenders();
        return it.hasNext();
    }

}
