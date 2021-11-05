package com.haulmont.task;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static com.haulmont.task.Status.DELIVERED;
import static com.haulmont.task.Status.ERROR;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class MailServiceMockedTest {

    private MailService mailService;
    @Mock
    private MailSender mailSender;

    @Before
    public void setUp() {
        mailService = new MailService(mailSender, new MessageRepository());
    }

    @Test
    public void testMailServiceWhenSendingSuccessfully() {
        Mockito.when(mailSender.sendMessage(any(), any(), any(), any())).thenReturn(true);
        Message message = new Message("Message subject", new Person(), new Person(), "Message text");
        assertTrue(mailService.sendMessage(message));
        assertEquals(DELIVERED, message.getStatus());
    }

    @Test
    public void testMailServiceWhenSendingFailed() {
        Mockito.when(mailSender.sendMessage(any(), any(), any(), any())).thenReturn(false);
        Message message = new Message("Message subject", new Person(), new Person(), "Message text");
        assertFalse(mailService.sendMessage(message));
        assertEquals(ERROR, message.getStatus());
    }

    @Test
    public void testMethodCalled() {
        Message message = new Message("Message subject", new Person(), new Person(), "Message text");
        assertFalse(mailService.sendMessage(message));
        Mockito.verify(mailSender, Mockito.times(1)).sendMessage(any(), any(), any(), any());
    }

    @Test
    public void testMethodCalledWithExpectedParameters() {
        Person from = new Person();
        from.setEmail("from@mail.com");
        Person to = new Person();
        to.setEmail("to@mail.com");
        Message message = new Message("Message subject", from, to, "Message text");
        assertFalse(mailService.sendMessage(message));
        Mockito.verify(mailSender, Mockito.times(1)).sendMessage("from@mail.com", "to@mail.com", "Message subject", "Message text");
    }

    @Test
    public void testRemoveMessage() {
        MessageRepository messageRepositoryMock = Mockito.mock(MessageRepository.class);
        mailService = new MailService(mailSender, messageRepositoryMock);
        Mockito.doNothing().when(messageRepositoryMock).removeMessage(any());
        Message message = new Message("Message subject", new Person(), new Person(), "Message text");
        mailService.removeMessage(message);
    }

}
