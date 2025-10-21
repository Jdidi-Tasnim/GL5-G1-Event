package tn.esprit.eventsproject.services;

import tn.esprit.eventsproject.entities.User;
import java.util.List;

public interface IUserService {
    User addUser(User user);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
    User getUser(Long id);
    List<User> getAllUsers();
}
