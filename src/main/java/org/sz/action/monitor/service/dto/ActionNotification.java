package org.sz.action.monitor.service.dto;

/**
 * This DTO is used when {@link org.sz.action.monitor.service.ActionMonitorService} processes actions.
 */
public class ActionNotification {

    private String channelName;
    private String message;

    /**
     * Constructor.
     */
    public ActionNotification() {
    }

    /**
     * Constructor.
     *
     * @param channelName DB notification channel name
     * @param message     text from stored procedure
     */
    public ActionNotification(String channelName, String message) {
        this.channelName = channelName;
        this.message = message;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
