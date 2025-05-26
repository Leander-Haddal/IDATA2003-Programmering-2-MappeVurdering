package no.ntnu.exception;

/**
 * Signals that data is invalid or malformed.
 */
public class InvalidDataException extends Exception {

    /**
     * Create exception with a message.
     */
    public InvalidDataException(String message) {
        super(message);
    }

    /**
     * Create exception with a message and root cause.
     */
    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
