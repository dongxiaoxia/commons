package xyz.dongxiaoxia.commons.persistence.uya.jdbc.exceptions;

/**
 * Created by dongxiaoxia on 2016/7/21.
 */
public class TooManyResultException extends RuntimeException {
    public TooManyResultException() {
    }

    public TooManyResultException(String message) {
        super(message);
    }

    public TooManyResultException(String message, Throwable cause) {
        super(message, cause);
    }

    public TooManyResultException(Throwable cause) {
        super(cause);
    }
}
