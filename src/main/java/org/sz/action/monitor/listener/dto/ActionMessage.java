package org.sz.action.monitor.listener.dto;

import java.util.Objects;

public class ActionMessage {

    private String text;
    private String channel;

    public ActionMessage() {
    }

    public ActionMessage(String text, String channel) {
        this.text = text;
        this.channel = channel;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActionMessage that = (ActionMessage) o;
        return Objects.equals(text, that.text) &&
                Objects.equals(channel, that.channel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, channel);
    }

    @Override
    public String toString() {
        return "ActionMessage{" +
                "text='" + text + '\'' +
                ", channel='" + channel + '\'' +
                '}';
    }
}
