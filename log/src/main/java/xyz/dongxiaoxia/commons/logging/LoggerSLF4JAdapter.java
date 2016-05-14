package xyz.dongxiaoxia.commons.logging;

import org.slf4j.LoggerFactory;

/**
 * slf4j日志适配器
 *
 * @author dongxiaoxia
 * @create 2016-05-14 2:45
 */
public class LoggerSLF4JAdapter implements LoggerAdapter {

    private org.slf4j.Logger logger;

    public LoggerSLF4JAdapter(Class clazz){
        logger = LoggerFactory.getLogger(clazz);
    }

    public void info(String message) {
        if (logger.isInfoEnabled()){
            logger.info(message);
        }
    }

    public void info(String message, Throwable t) {
        if (logger.isInfoEnabled()){
            logger.info(message,t);
        }
    }

    public void error(String message) {
        if (logger.isErrorEnabled()){
            logger.error(message);
        }
    }

    public void error(Throwable t) {
        if (logger.isErrorEnabled()){
            logger.error(t.getMessage(),t);
        }
    }

    public void error(String message, Throwable t) {
        if (logger.isErrorEnabled()){
            logger.error(message,t);
        }
    }

    public void debug(String message) {
        if (logger.isDebugEnabled()){
            logger.debug(message);
        }
    }

    public void debug(String message, Throwable t) {
        if (logger.isDebugEnabled()){
            logger.debug(message,t);
        }
    }

    public void warn(String message) {
        if (logger.isWarnEnabled()){
            logger.warn(message);
        }
    }

    public void warn(String message, Throwable t) {
        if (logger.isWarnEnabled()){
            logger.warn(message,t);
        }
    }
}
