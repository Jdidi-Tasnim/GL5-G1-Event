package tn.esprit.eventsproject.services;

import tn.esprit.eventsproject.entities.Message;
import java.util.List;

public interface IMessageService {
    Message sendMessage(Message message);
    List<Message> getMessagesForUser(String username);
    void deleteMessage(Long id);
}
