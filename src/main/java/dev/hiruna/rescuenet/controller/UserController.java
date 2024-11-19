package dev.hiruna.rescuenet.controller;

import dev.hiruna.rescuenet.dto.UserDTO;
import dev.hiruna.rescuenet.service.UserService;
import dev.hiruna.rescuenet.utill.JWTAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTAuthenticator jwtAuthenticator;

    // Helper method to check if the user has the "Admin" role
    private boolean isAdmin(String authHeader) {
        if (jwtAuthenticator.validateJwtToken(authHeader)) {
            Map<String, Object> payload = jwtAuthenticator.getJwtPayload(authHeader);
            if (payload != null && payload.containsKey("role")) {
                String role = (String) payload.get("role");
                return "Admin".equalsIgnoreCase(role);
            }
        }
        return false;
    }

    // **Register User (Admin Only)**
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestHeader("Authorization") String authHeader, @RequestBody UserDTO userDTO) {
        if (!isAdmin(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        try {
            UserDTO registeredUser = (UserDTO) userService.register(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    // **Forgot Password**
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        String response = userService.sendResetCode(email);
        if (response.startsWith("Reset code sent")) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // **Verify Reset Code**
    @PostMapping("/verify-reset-code")
    public ResponseEntity<String> verifyResetCode(@RequestParam String email, @RequestParam String resetCode, @RequestParam String newPassword) {
        boolean isVerified = userService.verifyResetCode(email, resetCode, newPassword);
        if (isVerified) {
            return ResponseEntity.ok("Password reset successfully.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid reset code.");
    }

    // **Verify JWT**
    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyJwt(@RequestHeader("Authorization") String authHeader) {
        if (jwtAuthenticator.validateJwtToken(authHeader)) {
            Map<String, Object> payload = jwtAuthenticator.getJwtPayload(authHeader);
            return ResponseEntity.ok(payload); // Return the JWT claims (payload)
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    // **Get All Users (Admin Only)**
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers(@RequestHeader("Authorization") String authHeader) {
        if (isAdmin(authHeader)) {
            List<UserDTO> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    // **Get User by ID**
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@RequestHeader("Authorization") String authHeader, @PathVariable Integer id) {
        if (jwtAuthenticator.validateJwtToken(authHeader)) {
            UserDTO user = userService.getUserById(id);
            if (user != null) {
                return ResponseEntity.ok(user);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    // **Update User**
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@RequestHeader("Authorization") String authHeader, @PathVariable Integer id, @RequestBody UserDTO userDTO) {
        if (jwtAuthenticator.validateJwtToken(authHeader)) {
            UserDTO updatedUser = userService.updateUser(id, userDTO);
            if (updatedUser != null) {
                return ResponseEntity.ok(updatedUser);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    // **Delete User (Admin Only)**
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@RequestHeader("Authorization") String authHeader, @PathVariable Integer id) {
        if (isAdmin(authHeader)) {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}