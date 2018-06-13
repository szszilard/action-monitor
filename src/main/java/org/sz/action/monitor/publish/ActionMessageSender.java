package org.sz.action.monitor.publish;

public interface ActionMessageSender {

    void sendMessageToTopic(String message, String topic);
}
