package org.sz.action.monitor.service;

/**
 * Interface for processing and publishing actions of a database table.
 */
public interface ActionMonitorService {

    /**
     * Retrieves DB table action notifications and publishes them.
     */
    void processAndPublish();
}
