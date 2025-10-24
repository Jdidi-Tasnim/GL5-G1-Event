package tn.esprit.eventsproject.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.eventsproject.entities.Message;
import tn.esprit.eventsproject.repositories.MessageRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MessageServiceImplTest {
    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageServiceImpl messageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendMessage() {
        Message message = new Message("alice", "bob", "Hello", LocalDateTime.now());
        when(messageRepository.save(any(Message.class))).thenReturn(message);
        Message result = messageService.sendMessage(message);
        assertEquals("alice", result.getSender());
        assertEquals("bob", result.getReceiver());
        assertEquals("Hello", result.getContent());
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    void testGetMessagesForUser() {
        Message m1 = new Message("alice", "bob", "Hi", LocalDateTime.now());
        Message m2 = new Message("bob", "alice", "Hey", LocalDateTime.now());
        when(messageRepository.findAll()).thenReturn(Arrays.asList(m1, m2));
        List<Message> messages = messageService.getMessagesForUser("alice");
        assertEquals(2, messages.size());
    }

    @Test
    void testDeleteMessage() {
        doNothing().when(messageRepository).deleteById(1L);
        messageService.deleteMessage(1L);
        verify(messageRepository, times(1)).deleteById(1L);
    }
}
