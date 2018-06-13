package org.sz.action.monitor.listener.dto;

public class Notification {

    private String type;
    private Client client;

    public Notification() {
    }

    public Notification(String type, Client client) {
        this.type = type;
        this.client = client;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "type='" + type + '\'' +
                ", client=" + client +
                '}';
    }
}
