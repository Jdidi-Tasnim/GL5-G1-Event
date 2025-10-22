package tn.esprit.eventsproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.eventsproject.dto.UserDTO;
import tn.esprit.eventsproject.entities.User;
import tn.esprit.eventsproject.mappers.EntityMapper;
import tn.esprit.eventsproject.services.IUserService;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
    @Autowired
    private IUserService userService;
    
    @Autowired
    private EntityMapper entityMapper;

    @PostMapping
    public UserDTO addUser(@RequestBody UserDTO userDTO) {
        // For the new user, we need to handle password separately
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        // Password should be handled securely in a real application
        User savedUser = userService.addUser(user);
        return entityMapper.toUserDTO(savedUser);
    }

    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        try {
            // First get the existing user to preserve any fields not in DTO
            User existingUser = userService.getUser(id);
            if (existingUser != null) {
                existingUser.setUsername(userDTO.getUsername());
                existingUser.setEmail(userDTO.getEmail());
                // Keep the existing password unless specifically changing it
                User updatedUser = userService.updateUser(id, existingUser);
                return entityMapper.toUserDTO(updatedUser);
            }
        } catch (Exception e) {
            // Log the exception instead of empty catch block
            System.err.println("Error updating user with ID " + id + ": " + e.getMessage());
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        return entityMapper.toUserDTO(user);
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return entityMapper.toUserDTOList(users);
    }
}