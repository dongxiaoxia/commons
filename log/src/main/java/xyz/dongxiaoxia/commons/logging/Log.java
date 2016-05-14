package xyz.dongxiaoxia.commons.logging;

/**
 * Created by dongxiaoxia on 2016/5/14.
 *
 * 日志类
 *
 * 提供日志统一的访问接口，已便适配不同的日志工具。
 */
public interface Log {
    void info(String message);

    void info(String message, Throwable t);

    void error(String message);

    void error(Throwable t);

    void error(String message, Throwable t);

    void debug(String message);

    void debug(String message, Throwable t);

    void warn(String message);

    void warn(String message, Throwable t);
}
