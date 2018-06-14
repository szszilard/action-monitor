package org.sz.action.monitor.service;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.sz.action.monitor.listener.DatabaseListener;
import org.sz.action.monitor.listener.dto.ActionMessage;
import org.sz.action.monitor.publisher.ActionMessagePublisher;
import org.sz.action.monitor.service.dto.ActionNotification;

import java.util.ArrayList;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ActionMonitorServiceImplTest {

    private static final String TEST_TOPIC_NAME = "testTopicName";
    private static final String TEST_CHANNEL_NAME = "testChannelName";

    @Mock
    private DatabaseListener mockedDatabaseListener;

    @Mock
    private ActionMessagePublisher mockedActionMessagePublisher;

    private ActionMonitorService actionMonitorService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        actionMonitorService = new ActionMonitorServiceImpl(mockedActionMessagePublisher, mockedDatabaseListener, TEST_TOPIC_NAME);
    }

    @Test
    public void notificationsShouldNotBeSentWithoutDbNotification() {
        when(mockedDatabaseListener.pollNotifications()).thenReturn(new ArrayList<>());

        actionMonitorService.processAndPublish();

        verify(mockedDatabaseListener, times(1)).pollNotifications();
        verifyZeroInteractions(mockedActionMessagePublisher);


    }

    @Test
    public void actionMessageShouldBeSentSuccessfullyIfDbNotificationReceived() throws Exception {
        ActionNotification actionNotification = new ActionNotification(TEST_CHANNEL_NAME, IOUtils.toString(this.getClass().getResourceAsStream("/insert_notification_payload.json"), "UTF-8"));
        when(mockedDatabaseListener.pollNotifications()).thenReturn(Collections.singletonList(actionNotification));

        actionMonitorService.processAndPublish();

        ArgumentCaptor<ActionMessage> captor = ArgumentCaptor.forClass(ActionMessage.class);
        verify(mockedActionMessagePublisher).sendMessageToTopic(captor.capture(), eq(TEST_TOPIC_NAME));
        ActionMessage actualActionMessage = captor.getValue();
        assertThat(actualActionMessage.getText(), containsString("Timestamp="));
        assertThat(actualActionMessage.getText(), containsString(":: a row with ID="));
        assertThat(actualActionMessage.getText(), containsString("10001"));
        assertThat(actualActionMessage.getText(), containsString("has been inserted"));
        assertThat(actualActionMessage.getChannel(), is(TEST_CHANNEL_NAME));
    }
}
