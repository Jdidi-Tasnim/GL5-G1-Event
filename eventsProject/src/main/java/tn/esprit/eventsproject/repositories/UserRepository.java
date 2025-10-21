package tn.esprit.eventsproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.eventsproject.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
