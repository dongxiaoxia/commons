package xyz.dongxiaoxia.commons.persistence.uya.jdbc.exceptions;

/**
 * Created by dongxiaoxia on 2016/7/21.
 */
public class PersistenceException extends RuntimeException {
    public PersistenceException() {
    }

    public PersistenceException(String message) {
        super(message);
    }

    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistenceException(Throwable cause) {
        super(cause);
    }
}
