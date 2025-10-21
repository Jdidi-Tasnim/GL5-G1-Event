package tn.esprit.eventsproject.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.eventsproject.entities.User;
import tn.esprit.eventsproject.services.IUserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    private IUserService userService;

    @Test
    @Transactional
    public void testAddUser() {
        // Create a user with Tunisian name
        User user = new User();
        user.setUsername("FaresTriki");
        user.setEmail("fares.triki@esprit.tn");
        user.setPassword("password123");

        // Save the user
        User savedUser = userService.addUser(user);

        // Assertions
        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals("FaresTriki", savedUser.getUsername());
        assertEquals("fares.triki@esprit.tn", savedUser.getEmail());
    }

    @Test
    @Transactional
    public void testUpdateUser() {
        // First create a user
        User user = new User();
        user.setUsername("AmeniChebbi");
        user.setEmail("ameni.chebbi@esprit.tn");
        user.setPassword("password123");
        User savedUser = userService.addUser(user);
        Long userId = savedUser.getId();

        // Update the user
        User updatedUser = new User();
        updatedUser.setUsername("AmeniUpdated");
        updatedUser.setEmail("ameni.updated@esprit.tn");
        updatedUser.setPassword("newpassword456");
        
        User result = userService.updateUser(userId, updatedUser);

        // Assertions
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("AmeniUpdated", result.getUsername());
        assertEquals("ameni.updated@esprit.tn", result.getEmail());
    }

    @Test
    @Transactional
    public void testDeleteUser() {
        // First create a user
        User user = new User();
        user.setUsername("MouradMeddeb");
        user.setEmail("mourad.meddeb@esprit.tn");
        user.setPassword("password123");
        User savedUser = userService.addUser(user);
        Long userId = savedUser.getId();

        // Delete the user
        userService.deleteUser(userId);

        // Verify user is deleted
        User deletedUser = userService.getUser(userId);
        assertNull(deletedUser);
    }

    @Test
    @Transactional
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
        assertTrue(users.size() >= 2);
        
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
}
