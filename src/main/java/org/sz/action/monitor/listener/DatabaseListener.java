package org.sz.action.monitor.listener;

import org.sz.action.monitor.service.dto.ActionNotification;

import java.util.List;

/**
 * Interface for listening database notifications.
 */
public interface DatabaseListener {

    /**
     * This method polls the database in a scheduled manner for DB procedures.
     *
     * @return list of notification messages
     */
    List<ActionNotification> pollNotifications();
}
