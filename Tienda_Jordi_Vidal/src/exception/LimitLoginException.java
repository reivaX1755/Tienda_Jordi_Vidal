package exception;

public class LimitLoginException extends Exception {

    public LimitLoginException(String message) {
        super(message);
    }

    public LimitLoginException(String message, Throwable cause) {
        super(message, cause);
    }
}
