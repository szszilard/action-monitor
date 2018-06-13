package org.sz.action.monitor.publish;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.stereotype.Component;
import org.sz.action.monitor.listener.dto.ActionMessage;

@Component
public class ActionMessageSenderImpl implements ActionMessageSender {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MessageSendingOperations<String> messageSendingOperations;

    @Override
    public void sendMessageToTopic(ActionMessage actionMessage, String topic) {
        LOGGER.info("Sending message: [{}] to [{}]", actionMessage.getText(), topic);
        messageSendingOperations.convertAndSend(topic, actionMessage);

    }
}
