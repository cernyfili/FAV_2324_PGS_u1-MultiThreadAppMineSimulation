
import org.apache.logging.log4j.LogManager;

import java.util.StringJoiner;

/**
 This class provides a static method to create logs with log4j for output file.
 The logs contain information about the class name, the ID of the current thread, the message to log, and the time it took (if provided).
 */
public class MyLogger {


    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger();

    /**
     Logs a message with the class name, the ID of the current thread, the message, and the time it took (if provided).
     @param className the name of the class from which the log is being created
     @param message the message to log
     @param time the time it took to execute the corresponding method (in milliseconds), or null if not available
     */
    public synchronized static void logMassage(String className,String message, Long time) {
        StringJoiner stringJoiner = new StringJoiner("\"\t\"", "\"", "\"");

        stringJoiner.add(className);

        stringJoiner.add(String.valueOf(Thread.currentThread().getId()));

        stringJoiner.add(message);

        if(time != null) stringJoiner.add(time.toString());

        String out = stringJoiner.toString();

        logger.info(out);

    }

    public synchronized static void error(String message){
        logger.error(message);
    }

    /**

     Logs a message with the class name and the message, without timing information.
     @param className the name of the class from which the log is being created
     @param message the message to log
     */
    public synchronized static void logMassage(String className,String message){
        logMassage(className, message, null);
    }
}
