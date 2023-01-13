package utils.logs;

import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.LoggerContext;
import utils.Util;

import java.io.File;
import java.net.URI;

public class Log {

//    private static final Logger Log = Logger.getLogger(Log.class.getName());
//    PropertyConfigurator.configure(getClass().getResource(Util.pathProject+"/src/test/resources/log4j2.properties"));
    //Initialize Log4j instance
    //Initialize Log4j instance
    private static final Logger Log = createLogger();
    static Logger createLogger() {
        System.setProperty("log4j2.loggerContextFactory", "org.apache.logging.log4j.core.impl.Log4jContextFactory");
        return LogManager.getLogger(Log.class.getName());
    }
//    private static final Logger Log =  LogManager.getLogger(utils.logs.Log.class);

    public static void startTestCase(String sTestCaseName){
        Log.info("****************************************************************************************");
        Log.info("$$$$$$$$$$$$$$$$$$$$$                 "+sTestCaseName+ "       $$$$$$$$$$$$$$$$$$$$$$$$$");
        Log.info("****************************************************************************************");
    }
    //Info Level Logs
    public static void info (String message) {
        Log.info(message);
    }
    public static void info (Object object) {
        Log.info(object);}

    //Warn Level Logs
    public static void warn (String message) {
        Log.warn(message);}
    public static void warn (Object object) {
        Log.warn(object);}

    //Error Level Logs
    public static void error (String message) {
        Log.error(message);}
    public static void error (Object object) {
        Log.error(object);}

    //Fatal Level Logs
    public static void fatal (String message) {
        Log.fatal(message);}

    //Debug Level Logs
    public static void debug (String message) {
        Log.debug(message);}
    public static void debug (Object object) {
        Log.debug(object);}
}
