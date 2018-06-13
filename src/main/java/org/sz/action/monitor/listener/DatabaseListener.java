package org.sz.action.monitor.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.sz.action.monitor.exception.DatabaseListeningException;
import org.sz.action.monitor.listener.dto.Notification;
import org.sz.action.monitor.publish.ActionMessageSender;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@EnableScheduling
public class DatabaseListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final int POLLING_FREQUENCY_IN_MSEC = 5000;
    private static final String ACTION_NOTIFICATION_CHANNEL_NAME = "action_notification";
    private static final String LISTENER_CREATION_STATEMENT = "LISTEN " + ACTION_NOTIFICATION_CHANNEL_NAME;

    private static final String LOG_MESSAGE_TIMESTAMP_PREFIX = "Timestamp=";
    private static final String LOG_MESSAGE_DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final String LOG_MESSAGE_ROW_ID_PART = " :: a row with ID=";
    private static final String LOG_MESSAGE_VERB = " has been ";

    private static final String DUMMY_STATEMENT = "SELECT 1";
    private static final String NOTIFICATION_EXCEPTION_MESSAGE = "Exception while receiving notification";

    private Connection connection;
    private ActionMessageSender actionMessageSender;
    private String topic;
    private Statement listenStatement;

    public DatabaseListener(@Autowired Connection connection, @Autowired ActionMessageSender actionMessageSender, @Value("${action-monitor.topic}") String topic) {
        this.connection = connection;
        this.actionMessageSender = actionMessageSender;
        this.topic = topic;
    }

    @Scheduled(fixedDelay = POLLING_FREQUENCY_IN_MSEC)
    public void pollNotifications() {
        try {
            createListenStatementIfRequired();
            getPendingNotifications();
        } catch (Exception e) {
            logger.error(NOTIFICATION_EXCEPTION_MESSAGE, e);
            throw new DatabaseListeningException(NOTIFICATION_EXCEPTION_MESSAGE, e);
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

    private void getPendingNotifications() throws SQLException, IOException {
        executeDummyStatementToContactDb();
        PGNotification[] notifications = getNotifications();
        if (notifications != null) {
            handleEachNotification(notifications);
        }
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

    private void handleEachNotification(PGNotification[] notifications) throws IOException {
        for (PGNotification pgNotification : notifications) {
            handleNotification(pgNotification);
        }
    }

    private void handleNotification(PGNotification pgNotification) throws IOException {
        String notificationParameter = pgNotification.getParameter();
        logger.info("Notification received on channel [{}] with payload [{}]", pgNotification.getName(), notificationParameter);
        Notification notification = new ObjectMapper().readValue(notificationParameter, Notification.class);
        String actionMessage = createActionMessageFrom(notification);
        logger.info(actionMessage);
        actionMessageSender.sendMessageToTopic(actionMessage, topic);
    }

    private String createActionMessageFrom(Notification notification) {
        return new StringBuilder(LOG_MESSAGE_TIMESTAMP_PREFIX)
                .append(LocalDateTime.now().format(DateTimeFormatter.ofPattern(LOG_MESSAGE_DATE_FORMAT_PATTERN)))
                .append(LOG_MESSAGE_ROW_ID_PART)
                .append(notification.getClient().getId())
                .append(LOG_MESSAGE_VERB)
                .append(ActionType.valueOf(notification.getType().toUpperCase()).getLogMessageVerb())
                .toString();
    }

    @PreDestroy
    public void destroy() throws SQLException {
        logger.info("Closing connection...");
        connection.close();
    }
}
