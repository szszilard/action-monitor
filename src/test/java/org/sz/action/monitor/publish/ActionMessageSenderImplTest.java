package org.sz.action.monitor.publish;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringJUnit4ClassRunner.class)
public class ActionMessageSenderImplTest {

    private static final String TEST_MESSAGE = "testMessage";
    private static final String TEST_TOPIC_NAME = "testTopicName";

    @Mock
    private MessageSendingOperations<String> messageSendingOperations;

    @InjectMocks
    private ActionMessageSenderImpl actionMessageSender;

    @Test
    public void actionMessageShouldBeSentSuccessfully() {
        actionMessageSender.sendMessageToTopic(TEST_MESSAGE, TEST_TOPIC_NAME);
        verify(messageSendingOperations, times(1)).convertAndSend(TEST_TOPIC_NAME, TEST_MESSAGE);
    }
}
