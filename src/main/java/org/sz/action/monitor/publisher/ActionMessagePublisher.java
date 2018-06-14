package org.sz.action.monitor.publisher;

import org.sz.action.monitor.listener.dto.ActionMessage;

/**
 * Interface for publishing action messages.
 */
public interface ActionMessagePublisher {

    /**
     * Send message to topic with the configured broker.
     *
     * @param actionMessage contains the action
     * @param topic         topic where the message will be published
     */
    void sendMessageToTopic(ActionMessage actionMessage, String topic);
}
