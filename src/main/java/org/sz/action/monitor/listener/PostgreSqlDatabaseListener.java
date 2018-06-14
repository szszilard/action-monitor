package org.sz.action.monitor.listener;

import org.postgresql.PGConnection;
import org.postgresql.PGNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sz.action.monitor.exception.DatabaseListenerException;
import org.sz.action.monitor.service.dto.ActionNotification;

import javax.annotation.PreDestroy;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Listener for PostgreSQL database.
 */
@Component
public class PostgreSqlDatabaseListener implements DatabaseListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String ACTION_NOTIFICATION_CHANNEL_NAME = "action_notification";
    private static final String LISTENER_CREATION_STATEMENT = "LISTEN " + ACTION_NOTIFICATION_CHANNEL_NAME;
    private static final String DUMMY_STATEMENT = "SELECT 1";

    private static final String NOTIFICATION_EXCEPTION_MESSAGE = "Exception while receiving notification";

    private Connection connection;
    private Statement listenStatement;

    public PostgreSqlDatabaseListener(@Autowired Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<ActionNotification> pollNotifications() {
        try {
            createListenStatementIfRequired();
            return getPendingNotifications();
        } catch (Exception e) {
            logger.error(NOTIFICATION_EXCEPTION_MESSAGE, e);
            throw new DatabaseListenerException(NOTIFICATION_EXCEPTION_MESSAGE, e);
        }
    }

    private void createListenStatementIfRequired() throws SQLException {
        if (listenStatement == null) {
            executeNewListenerStatement();
        }
    }

    private void executeNewListenerStatement() throws SQLException {
        listenStatement = connection.createStatement();
        listenStatement.executeUpdate(LISTENER_CREATION_STATEMENT);
        listenStatement.close();
    }

    private List<ActionNotification> getPendingNotifications() throws SQLException {
        executeDummyStatementToContactDb();
        PGNotification[] notifications = getNotifications();

        List<ActionNotification> actionNotificationList = new ArrayList<>();
        if (notifications != null) {
            for (PGNotification pgNotification : notifications) {
                logger.debug("Notification received on channel [{}] with payload [{}]", pgNotification.getName(), pgNotification.getParameter());
                ActionNotification actionNotification = new ActionNotification(pgNotification.getName(), pgNotification.getParameter());
                actionNotificationList.add(actionNotification);
            }
        }
        return actionNotificationList;
    }

    PGNotification[] getNotifications() throws SQLException {
        return ((PGConnection) connection).getNotifications();
    }

    private void executeDummyStatementToContactDb() throws SQLException {
        Statement selectStatement = connection.createStatement();
        ResultSet resultSet = selectStatement.executeQuery(DUMMY_STATEMENT);
        resultSet.close();
        selectStatement.close();
    }


    @PreDestroy
    public void destroy() throws SQLException {
        logger.info("Closing connection...");
        connection.close();
    }
}
