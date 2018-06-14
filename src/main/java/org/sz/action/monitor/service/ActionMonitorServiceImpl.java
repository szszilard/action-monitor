package org.sz.action.monitor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.sz.action.monitor.exception.ActionMonitorException;
import org.sz.action.monitor.listener.DatabaseListener;
import org.sz.action.monitor.listener.action.ActionType;
import org.sz.action.monitor.listener.dto.ActionMessage;
import org.sz.action.monitor.listener.dto.DbNotification;
import org.sz.action.monitor.publisher.ActionMessagePublisher;
import org.sz.action.monitor.service.dto.ActionNotification;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Scheduled service to receive DB notifications and publish them to topic.
 */
@Service
@EnableScheduling
public class ActionMonitorServiceImpl implements ActionMonitorService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final int POLLING_FREQUENCY_IN_MSEC = 5000;

    private static final String LOG_MESSAGE_TIMESTAMP_PREFIX = "Timestamp=";
    private static final String LOG_MESSAGE_DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final String LOG_MESSAGE_ROW_ID_PART = " :: a row with ID=";
    private static final String LOG_MESSAGE_VERB = " has been ";

    private ActionMessagePublisher actionMessagePublisher;
    private DatabaseListener databaseListener;
    private String topic;

    public ActionMonitorServiceImpl(@Autowired ActionMessagePublisher actionMonitorService, @Autowired DatabaseListener databaseListener, @Value("${action-monitor.topic}") String topic) {
        this.actionMessagePublisher = actionMonitorService;
        this.databaseListener = databaseListener;
        this.topic = topic;
    }

    @Override
    @Scheduled(fixedDelay = POLLING_FREQUENCY_IN_MSEC)
    public void processAndPublish() {
        try {
            List<ActionNotification> actionNotificationList = databaseListener.pollNotifications();
            handleEachNotification(actionNotificationList);
        } catch (Exception e) {
            logger.error("Exception while processing action notifications", e);
            throw new ActionMonitorException("Exception while processing action notifications", e);
        }
    }

    private void handleEachNotification(List<ActionNotification> notifications) throws IOException {
        for (ActionNotification pgNotification : notifications) {
            handleNotification(pgNotification);
        }
    }

    private void handleNotification(ActionNotification actionNotification) throws IOException {
        DbNotification dbNotification = new ObjectMapper().readValue(actionNotification.getMessage(), DbNotification.class);
        String actionMessageText = createActionMessageFrom(dbNotification);
        ActionMessage actionMessage = new ActionMessage(actionMessageText, actionNotification.getChannelName());
        logger.info(actionMessageText);
        actionMessagePublisher.sendMessageToTopic(actionMessage, topic);
    }

    private String createActionMessageFrom(DbNotification dbNotification) {
        return new StringBuilder(LOG_MESSAGE_TIMESTAMP_PREFIX)
                .append(LocalDateTime.now().format(DateTimeFormatter.ofPattern(LOG_MESSAGE_DATE_FORMAT_PATTERN)))
                .append(LOG_MESSAGE_ROW_ID_PART)
                .append(dbNotification.getClient().getId())
                .append(LOG_MESSAGE_VERB)
                .append(ActionType.valueOf(dbNotification.getType().toUpperCase()).getLogMessageVerb())
                .toString();
    }
}
