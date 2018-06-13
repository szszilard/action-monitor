package org.sz.action.monitor.exception;

public class DatabaseListeningException extends RuntimeException {

    public DatabaseListeningException(String message) {
        super(message);
    }

    public DatabaseListeningException(String message, Throwable cause) {
        super(message, cause);
    }
}
