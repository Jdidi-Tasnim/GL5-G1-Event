package tn.esprit.eventsproject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.eventsproject.entities.Message;
import tn.esprit.eventsproject.repositories.MessageRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageServiceImpl implements IMessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Override
    public Message sendMessage(Message message) {
        message.setTimestamp(LocalDateTime.now());
        return messageRepository.save(message);
    }

    @Override
    public List<Message> getMessagesForUser(String username) {
        return messageRepository.findAll().stream()
                .filter(m -> m.getSender().equals(username) || m.getReceiver().equals(username))
                .toList();
    }

    @Override
    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }
}
