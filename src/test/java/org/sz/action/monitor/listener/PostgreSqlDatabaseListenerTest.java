package org.sz.action.monitor.listener;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.postgresql.PGNotification;
import org.sz.action.monitor.listener.dto.ActionMessage;
import org.sz.action.monitor.publish.ActionMessageSender;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PostgreSqlDatabaseListenerTest {

    private static final String TEST_TOPIC_NAME = "testTopicName";
    public static final String TEST_CHANNEL_NAME = "testChannelName";

    @Mock
    private ActionMessageSender mockedActionMessageSender;

    @Mock
    private Connection mockedConnection;

    @Mock
    private Statement mockedStatement;

    @Mock
    private ResultSet mockedResultSet;


    private PostgreSqlDatabaseListener databaseListener;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(mockedConnection.createStatement()).thenReturn(mockedStatement);
        when(mockedStatement.executeQuery(anyString())).thenReturn(mockedResultSet);
    }

    @Test
    public void noMessagesShouldBeSentWhenNoNotificationReceived() throws Exception {
        databaseListener = spy(new PostgreSqlDatabaseListener(mockedConnection, mockedActionMessageSender, TEST_TOPIC_NAME));
        doReturn(null).when(databaseListener).getNotifications();

        databaseListener.pollNotifications();

        verify(mockedConnection, times(2)).createStatement();
        verify(mockedStatement, times(1)).executeUpdate(anyString());
        verify(mockedStatement, times(1)).executeQuery(anyString());
        verify(mockedStatement, times(2)).close();
        verify(mockedResultSet, times(1)).close();
        verifyNoMoreInteractions(mockedConnection, mockedStatement, mockedResultSet);
        verifyZeroInteractions(mockedActionMessageSender);
    }

    @Test
    public void messageShouldBeSentWhenOneNotificationReceived() throws Exception {
        databaseListener = spy(new PostgreSqlDatabaseListener(mockedConnection, mockedActionMessageSender, TEST_TOPIC_NAME));
        String payload = IOUtils.toString(this.getClass().getResourceAsStream("/insert_notification_payload.json"), "UTF-8");
        PGNotification[] pgNotifications = singletonList(buildTestNotification(TEST_CHANNEL_NAME, payload)).toArray(new PGNotification[1]);
        doReturn(pgNotifications).when(databaseListener).getNotifications();

        databaseListener.pollNotifications();

        verify(mockedConnection, times(2)).createStatement();
        verify(mockedStatement, times(1)).executeUpdate(anyString());
        verify(mockedStatement, times(1)).executeQuery(anyString());
        verify(mockedStatement, times(2)).close();
        verify(mockedResultSet, times(1)).close();
        ArgumentCaptor<ActionMessage> captor = ArgumentCaptor.forClass(ActionMessage.class);
        verify(mockedActionMessageSender).sendMessageToTopic(captor.capture(), eq(TEST_TOPIC_NAME));
        verifyNoMoreInteractions(mockedConnection, mockedStatement, mockedResultSet, mockedActionMessageSender);
        ActionMessage actualActionMessage = captor.getValue();
        assertThat(actualActionMessage.getText(), containsString("Timestamp="));
        assertThat(actualActionMessage.getText(), containsString(":: a row with ID="));
        assertThat(actualActionMessage.getText(), containsString("10001"));
        assertThat(actualActionMessage.getText(), containsString("has been inserted"));
        assertThat(actualActionMessage.getChannel(), is(TEST_CHANNEL_NAME));
    }

    private PGNotification buildTestNotification(String name, String parameterAsString) {
        return new PGNotification() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public int getPID() {
                return 42;
            }

            @Override
            public String getParameter() {
                return parameterAsString;
            }
        };
    }


}
