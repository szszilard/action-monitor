package org.sz.action.monitor.publisher;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.sz.action.monitor.listener.dto.ActionMessage;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringJUnit4ClassRunner.class)
public class ActionMessagePublisherImplTest {

    private static final String TEST_MESSAGE = "testMessage";
    private static final String TEST_TOPIC_NAME = "testTopicName";
    private static final String TEST_CHANNEL = "TEST_CHANNEL";

    @Mock
    private MessageSendingOperations<String> messageSendingOperations;

    @InjectMocks
    private ActionMessagePublisherImpl actionMessageSender;

    @Test
    public void actionMessageShouldBeSentSuccessfully() {
        ActionMessage actionMessage = new ActionMessage(TEST_MESSAGE, TEST_CHANNEL);
        actionMessageSender.sendMessageToTopic(actionMessage, TEST_TOPIC_NAME);
        verify(messageSendingOperations, times(1)).convertAndSend(TEST_TOPIC_NAME, actionMessage);
    }
}
