package xyz.dongxiaoxia.commons.persistence.uya.jdbc.exceptions;

/**
 * Created by dongxiaoxia on 2016/7/21.
 */
public class DataAccessException extends RuntimeException {
    public DataAccessException() {
    }

    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataAccessException(Throwable cause) {
        super(cause);
    }
}
