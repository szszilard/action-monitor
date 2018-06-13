package org.sz.action.monitor.exception;

/**
 * This exception is thrown if anything goes wrong while listening database notifications.
 */
public class DatabaseListenerException extends RuntimeException {

    /**
     * Constructor.
     *
     * @param message message of the exception
     * @param cause   cause of the exception
     */
    public DatabaseListenerException(String message, Throwable cause) {
        super(message, cause);
    }
}
