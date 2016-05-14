package xyz.dongxiaoxia.commons.logging;

/**
 * @author dongxiaoxia
 * @create 2016-05-14 0:49
 */
public class Logger{
    private static final Log LOG = getLogger(Logger.class);

    public static Log getLogger(Class<?> clazz){
           return LoggerAdapterFactory.getLoggerAdapter(clazz);
    }

    public static void info(String message) {
        LOG.info(message);
    }
    
    public static void info(String message, Throwable t) {
        LOG.info(message,t);
    }

    public static void error(String message) {
        LOG.error(message);
    }

    public static void error(Throwable t) {
        LOG.error(t);
    }

    public static void  error(String message, Throwable t) {
        LOG.error(message,t);
    }

    public static void debug(String message) {
        LOG.debug(message);
    }

    public static void debug(String message, Throwable t) {
        LOG.debug(message,t);
    }

    public static void warn(String message) {
        LOG.warn(message);
    }

    public static void warn(String message, Throwable t) {
        LOG.warn(message,t);
    }
}
