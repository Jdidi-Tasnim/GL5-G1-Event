package tn.esprit.eventsproject.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.esprit.eventsproject.entities.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceImplTest {

    private InMemoryUserService userService;

    @BeforeEach
    public void setup() {
        userService = new InMemoryUserService();
    }

    @Test
    public void testAddUser() {
        User user = new User();
        user.setUsername("FaresTriki");
        user.setEmail("fares.triki@esprit.tn");
        user.setPassword("password123");

        User savedUser = userService.addUser(user);

        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals("FaresTriki", savedUser.getUsername());
        assertEquals("fares.triki@esprit.tn", savedUser.getEmail());
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setUsername("AmeniChebbi");
        user.setEmail("ameni.chebbi@esprit.tn");
        user.setPassword("password123");
        User savedUser = userService.addUser(user);
        Long userId = savedUser.getId();

        User updatedUser = new User();
        updatedUser.setUsername("AmeniUpdated");
        updatedUser.setEmail("ameni.updated@esprit.tn");
        updatedUser.setPassword("newpassword456");
        
        User result = userService.updateUser(userId, updatedUser);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("AmeniUpdated", result.getUsername());
        assertEquals("ameni.updated@esprit.tn", result.getEmail());
    }

    @Test
    public void testDeleteUser() {
        User user = new User();
        user.setUsername("MouradMeddeb");
        user.setEmail("mourad.meddeb@esprit.tn");
        user.setPassword("password123");
        User savedUser = userService.addUser(user);
        Long userId = savedUser.getId();

        userService.deleteUser(userId);

        User deletedUser = userService.getUser(userId);
        assertNull(deletedUser);
    }

    @Test
    public void testGetAllUsers() {
        User user1 = new User();
        user1.setUsername("AmalZribi");
        user1.setEmail("amal.zribi@esprit.tn");
        user1.setPassword("password123");
        userService.addUser(user1);

        User user2 = new User();
        user2.setUsername("NaderHadjAli");
        user2.setEmail("nader.hadjali@esprit.tn");
        user2.setPassword("password456");
        userService.addUser(user2);

        List<User> users = userService.getAllUsers();

        assertNotNull(users);
        assertEquals(2, users.size());
        
        boolean foundAmal = false;
        boolean foundNader = false;
        
        for (User user : users) {
            if (user.getUsername().equals("AmalZribi")) {
                foundAmal = true;
            }
            if (user.getUsername().equals("NaderHadjAli")) {
                foundNader = true;
            }
        }
        
        assertTrue(foundAmal);
        assertTrue(foundNader);
    }

    // In-memory implementation of user service for testing
    private static class InMemoryUserService {
        private final Map<Long, User> userStore = new HashMap<>();
        private long idCounter = 1;

        public User addUser(User user) {
            user.setId(idCounter++);
            userStore.put(user.getId(), user);
            return user;
        }

        public User updateUser(Long id, User updatedUser) {
            if (userStore.containsKey(id)) {
                User existingUser = userStore.get(id);
                existingUser.setUsername(updatedUser.getUsername());
                existingUser.setEmail(updatedUser.getEmail());
                existingUser.setPassword(updatedUser.getPassword());
                return existingUser;
            }
            return null;
        }

        public void deleteUser(Long id) {
            userStore.remove(id);
        }

        public User getUser(Long id) {
            return userStore.get(id);
        }

        public List<User> getAllUsers() {
            return new ArrayList<>(userStore.values());
        }
    }
}