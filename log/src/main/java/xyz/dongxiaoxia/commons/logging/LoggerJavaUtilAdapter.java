package xyz.dongxiaoxia.commons.logging;

import java.util.logging.Logger;

/**
 * java原生日志工具类日志适配器
 *
 * @author dongxiaoxia
 * @create 2016-05-14 2:47
 */
public class LoggerJavaUtilAdapter implements LoggerAdapter{

    private java.util.logging.Logger logger;

    public LoggerJavaUtilAdapter(Class clazz){
        logger = Logger.getLogger(clazz!=null?clazz.getName() :"");
    }

    public void info(String message) {
        logger.info(message);
    }

    public void info(String message, Throwable t) {
    }

    public void error(String message) {

    }

    public void error(Throwable t) {

    }

    public void error(String message, Throwable t) {

    }

    public void debug(String message) {

    }

    public void debug(String message, Throwable t) {

    }

    public void warn(String message) {

    }

    public void warn(String message, Throwable t) {

    }
}
