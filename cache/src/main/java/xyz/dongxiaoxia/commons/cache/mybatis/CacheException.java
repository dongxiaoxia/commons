package xyz.dongxiaoxia.commons.cache.mybatis;

/**
 * @author dongxiaoxia
 * @create 2016-07-08 11:56
 */
public class CacheException extends RuntimeException {

    public CacheException() {
    }

    public CacheException(String message) {
        super(message);
    }

    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacheException(Throwable cause) {
        super(cause);
    }

}
