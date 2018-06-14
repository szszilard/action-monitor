package org.sz.action.monitor.listener.dto;

/**
 * This DTO is used when DB notifications are transported.
 */
public class DbNotification {

    private String type;
    private Client client;

    /**
     * Constructor.
     */
    public DbNotification() {
    }

    /**
     * Constructor.
     *
     * @param type   database table action type: insert, update, delete
     * @param client row of the client table
     */
    public DbNotification(String type, Client client) {
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
        return "ActionNotification{" +
                "type='" + type + '\'' +
                ", client=" + client +
                '}';
    }
}
