package pl.ttpsc.lego.application;

import com.thingworx.communications.client.ClientConfigurator;
import com.thingworx.communications.client.ConnectedThingClient;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import pl.ttpsc.lego.communication.LegoBrickThing;
import pl.ttpsc.lego.exception.SystemEnvNotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import static pl.ttpsc.lego.utils.Env.getSysEnv;


public class RobotArmApplication {
    private final static Logger logger = Logger.getLogger(RobotArmApplication.class);


    public static Properties slf4jProperties;

    public static void main(String[] args) throws SystemEnvNotFoundException {

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Exited!");
            }
        });

        slf4jProperties = new Properties();
        setLogProp(getSysEnv("LOGGER_LEVEL"), getSysEnv("LOGGER_PATH"));

        logger.debug("Application Started");

        ClientConfigurator config = new ClientConfigurator();
        config.setUri(getSysEnv("THINGWORX_URI"));
        config.setAppKey(getSysEnv("THINGWORX_APPKEY"));
        config.ignoreSSLErrors(true);

        try {
            ConnectedThingClient client = new ConnectedThingClient(config);

            LegoBrickThing thing = new LegoBrickThing("A basic virtual thing for Lego Brick",
                                                      client);

            client.bindThing(thing);
            client.start();

            if (client.waitForConnection(30000)) {
                logger.info("The client is now connected.");

                while (!client.isShutdown()) {
                    Thread.sleep(500);
                    thing.processScanRequest();
                }
            } else {
                logger.warn("Client did not connect within 30 seconds. Exiting");
            }
        } catch (Exception e) {
            logger.error("An exception occurred during execution.", e);
        }
    }

    private static void setLogProp(String logLvl, String path) throws SystemEnvNotFoundException {

        if (getSysEnv("LOGGER_TARGET") == "FILE") {

            slf4jProperties.setProperty("log4j.rootLogger", logLvl + ", file,");
            slf4jProperties
                    .setProperty("log4j.appender.file", "org.apache.log4j.RollingFileAppender");
            slf4jProperties.setProperty("log4j.appender.file.File", path + "data_"
                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"))
                    + ".log");
            slf4jProperties
                    .setProperty("log4j.appender.file.layout", "org.apache.log4j.PatternLayout");
            slf4jProperties.setProperty(
                    "log4j.appender.file.layout.ConversionPattern",
                    "%d{yyyy-MM-dd HH:mm:ss.SSS} %-5p %c{1}:%L - %m%n");
        } else {
            //stdout
            slf4jProperties.setProperty("log4j.rootLogger", logLvl + ", console,");
            slf4jProperties
                    .setProperty("log4j.appender.console", "org.apache.log4j.ConsoleAppender");
            slf4jProperties.setProperty("log4j.appender.console.layout",
                                        "org.apache.log4j.PatternLayout");
            slf4jProperties.setProperty("log4j.appender.console.layout.ConversionPattern",
                                        "%d{yyyy-MM-dd HH:mm:ss.SSS} %-5p %c{1}:%L - %m%n");
        }
        PropertyConfigurator.configure(slf4jProperties);
    }

}
