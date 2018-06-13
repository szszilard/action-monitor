package org.sz.action.monitor.listener;

public enum ActionType {

    INSERT("inserted"),
    UPDATE("updated"),
    DELETE("deleted");

    private final String logMessageVerb;

    ActionType(String logMessageVerb) {
        this.logMessageVerb = logMessageVerb;
    }

    public String getLogMessageVerb() {
        return logMessageVerb;
    }
}
