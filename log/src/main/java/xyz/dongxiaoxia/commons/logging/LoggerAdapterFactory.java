package xyz.dongxiaoxia.commons.logging;

/**
 * 日志适配器实现类的工厂类
 *
 * 如果有需要的话，比如要添加新的日志适配器，可以重写这个类。
 *
 * @author dongxiaoxia
 * @create 2016-05-14 2:38
 */
public class LoggerAdapterFactory {
    public static LoggerAdapter getLoggerAdapter(Class<?> clazz){
        //根据具体场景可以重写此方法
        //默认采用slf4j日志工具
//        return new LoggerJavaUtilAdapter(clazz);
        return new LoggerSLF4JAdapter(clazz);
    }
}
