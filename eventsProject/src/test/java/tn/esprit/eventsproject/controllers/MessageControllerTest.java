package tn.esprit.eventsproject.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tn.esprit.eventsproject.entities.Message;
import tn.esprit.eventsproject.services.IMessageService;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MessageControllerTest {
    @Mock
    private IMessageService messageService;

    @InjectMocks
    private MessageController messageController;

    private MockMvc mockMvc;

    public MessageControllerTest() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(messageController).build();
    }

    @Test
    void testSendMessage() throws Exception {
        Message message = new Message("alice", "bob", "Hello", LocalDateTime.now());
        when(messageService.sendMessage(any(Message.class))).thenReturn(message);
        mockMvc.perform(post("/api/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"sender\":\"alice\",\"receiver\":\"bob\",\"content\":\"Hello\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sender").value("alice"));
    }

    @Test
    void testGetMessagesForUser() throws Exception {
        Message m1 = new Message("alice", "bob", "Hi", LocalDateTime.now());
        when(messageService.getMessagesForUser("alice")).thenReturn(Arrays.asList(m1));
        mockMvc.perform(get("/api/messages/alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sender").value("alice"));
    }

    @Test
    void testDeleteMessage() throws Exception {
        doNothing().when(messageService).deleteMessage(1L);
        mockMvc.perform(delete("/api/messages/1"))
                .andExpect(status().isOk());
    }
}
