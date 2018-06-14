package org.sz.action.monitor.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.postgresql.PGNotification;
import org.sz.action.monitor.listener.dto.DbNotification;
import org.sz.action.monitor.service.dto.ActionNotification;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PostgreSqlDatabaseListenerTest {

    private static final String TEST_CHANNEL_NAME = "testChannelName";

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
    public void emptyListShouldBeReturnedWhenNoNotificationReceived() throws Exception {
        databaseListener = spy(new PostgreSqlDatabaseListener(mockedConnection));
        doReturn(null).when(databaseListener).getNotifications();

        List<ActionNotification> actionNotificationList = databaseListener.pollNotifications();

        verify(mockedConnection, times(2)).createStatement();
        verify(mockedStatement, times(1)).executeUpdate(anyString());
        verify(mockedStatement, times(1)).executeQuery(anyString());
        verify(mockedStatement, times(2)).close();
        verify(mockedResultSet, times(1)).close();
        verifyNoMoreInteractions(mockedConnection, mockedStatement, mockedResultSet);

        assertThat(actionNotificationList.size(), is(0));
    }

    @Test
    public void singletonListShouldBeReturnedWhenOneNotificationReceived() throws Exception {
        databaseListener = spy(new PostgreSqlDatabaseListener(mockedConnection));
        String payload = IOUtils.toString(this.getClass().getResourceAsStream("/insert_notification_payload.json"), "UTF-8");
        PGNotification[] pgNotifications = singletonList(buildTestNotification(TEST_CHANNEL_NAME, payload)).toArray(new PGNotification[1]);
        doReturn(pgNotifications).when(databaseListener).getNotifications();

        List<ActionNotification> actionNotificationList = databaseListener.pollNotifications();

        verify(mockedConnection, times(2)).createStatement();
        verify(mockedStatement, times(1)).executeUpdate(anyString());
        verify(mockedStatement, times(1)).executeQuery(anyString());
        verify(mockedStatement, times(2)).close();
        verify(mockedResultSet, times(1)).close();
        verifyNoMoreInteractions(mockedConnection, mockedStatement, mockedResultSet);
        assertThat(actionNotificationList.size(), is(1));
        ActionNotification actionNotification = actionNotificationList.get(0);
        assertThat(actionNotification.getChannelName(), is(TEST_CHANNEL_NAME));
        DbNotification dbNotification = new ObjectMapper().readValue(actionNotification.getMessage(), DbNotification.class);
        assertThat(dbNotification.getClient().getId(), is(10001L));
        assertThat(dbNotification.getClient().getName(), is("John Free"));
        assertThat(dbNotification.getClient().getCity(), is("New York"));
        assertThat(dbNotification.getClient().getAge(), is(43));
        assertThat(dbNotification.getType(), is("insert"));
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
