package org.sz.action.monitor.publish;

import org.sz.action.monitor.listener.dto.ActionMessage;

public interface ActionMessageSender {

    void sendMessageToTopic(ActionMessage actionMessage, String topic);
}
