package tn.esprit.eventsproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.eventsproject.entities.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
