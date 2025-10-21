package tn.esprit.eventsproject.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.eventsproject.entities.User;
import tn.esprit.eventsproject.repositories.UserRepository;
import tn.esprit.eventsproject.services.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplMockitoTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setId(1L);
        user.setUsername("MohamedAziz");
        user.setEmail("mohamed.aziz@esprit.tn");
        user.setPassword("password123");
    }

    @Test
    public void testAddUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.addUser(user);

        assertNotNull(savedUser);
        assertEquals("MohamedAziz", savedUser.getUsername());
        assertEquals("mohamed.aziz@esprit.tn", savedUser.getEmail());
        
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testUpdateUser() {
        User updatedUser = new User();
        updatedUser.setUsername("YassineTN");
        updatedUser.setEmail("yassine.tn@esprit.tn");
        updatedUser.setPassword("newpassword123");
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User result = userService.updateUser(1L, updatedUser);

        assertNotNull(result);
        assertEquals("YassineTN", result.getUsername());
        assertEquals("yassine.tn@esprit.tn", result.getEmail());
        
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testDeleteUser() {
        userService.deleteUser(1L);
        
        verify(userRepository).deleteById(1L);
    }

    @Test
    public void testGetAllUsers() {
        List<User> userList = new ArrayList<>();
        
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("MohamedAziz");
        user1.setEmail("mohamed.aziz@esprit.tn");
        
        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("SalmaBS");
        user2.setEmail("salma.bs@esprit.tn");
        
        userList.add(user1);
        userList.add(user2);
        
        when(userRepository.findAll()).thenReturn(userList);
        
        List<User> result = userService.getAllUsers();
        
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("MohamedAziz", result.get(0).getUsername());
        assertEquals("SalmaBS", result.get(1).getUsername());
        
        verify(userRepository).findAll();
    }
}
