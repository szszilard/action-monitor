package org.sz.action.monitor.exception;

import org.sz.action.monitor.publisher.ActionMessagePublisher;

/**
 * This exception is thrown if anything goes wrong while sending notification in {@link ActionMessagePublisher}.
 */
public class ActionMessageSenderException extends RuntimeException {

    /**
     * Constructor.
     *
     * @param message message of the exception
     * @param cause   cause of the exception
     */
    public ActionMessageSenderException(String message, Throwable cause) {
        super(message, cause);
    }
}
