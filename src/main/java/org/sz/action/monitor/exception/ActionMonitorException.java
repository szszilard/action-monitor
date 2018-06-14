package org.sz.action.monitor.exception;

/**
 * This exception is thrown if anything goes wrong while processing action notifications.
 */
public class ActionMonitorException extends RuntimeException {

    /**
     * Constructor.
     *
     * @param message message of the exception
     * @param cause   cause of the exception
     */
    public ActionMonitorException(String message, Throwable cause) {
        super(message, cause);
    }
}
