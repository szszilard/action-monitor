package org.sz.action.monitor.listener.action;

/**
 * This enum contains constants of the monitored database table manipulation types.
 * The corresponding phrases for logging messages are also defined.
 */
public enum ActionType {

    INSERT("inserted"),
    UPDATE("updated"),
    DELETE("deleted");

    private final String logMessageVerb;

    /**
     * Constructor.
     *
     * @param logMessageVerb log message phrase for the given command
     */
    ActionType(String logMessageVerb) {
        this.logMessageVerb = logMessageVerb;
    }

    /**
     * Getter for the log message phrase.
     *
     * @return log message phrase
     */
    public String getLogMessageVerb() {
        return logMessageVerb;
    }
}
