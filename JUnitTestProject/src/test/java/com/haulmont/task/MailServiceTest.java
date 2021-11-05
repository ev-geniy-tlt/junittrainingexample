package com.haulmont.task;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import static com.haulmont.task.Status.DELIVERED;
import static com.haulmont.task.Status.ERROR;
import static org.junit.Assert.*;

public class MailServiceTest {

    private MailService mailService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testMailServiceWhenSendingSuccessfully() {
        setupMailService(true);
        Message message = new Message("Message subject", new Person(), new Person(), "Message text");
        assertTrue(mailService.sendMessage(message));
        assertEquals(DELIVERED, message.getStatus());
    }

    @Test
    public void testMailServiceWhenSendingFailed() {
        setupMailService(false);
        Message message = new Message("Message subject", new Person(), new Person(), "Message text");
        assertFalse(mailService.sendMessage(message));
        assertEquals(ERROR, message.getStatus());
    }

    @Test
    public void testIsExceptionThrown() {
        setupMailService(true);
        expectedException.expect(NotImplementedException.class);
        Message message = new Message("Message subject", new Person(), new Person(), "Message text");
        mailService.removeMessage(message);

    }


    private void setupMailService(boolean sendMessageResult) {
        mailService = new MailService((from, to, subject, text) -> sendMessageResult, new MessageRepository());
    }

}
