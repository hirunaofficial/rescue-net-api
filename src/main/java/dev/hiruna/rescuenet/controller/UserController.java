package dev.hiruna.rescuenet.controller;

import dev.hiruna.rescuenet.dto.*;
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
    public ResponseEntity<ResponseDTO<UserDTO>> registerUser(@RequestHeader("Authorization") String authHeader, @RequestBody UserDTO userDTO) {
        if (!isAdmin(authHeader)) {
            ErrorDTO error = new ErrorDTO(HttpStatus.FORBIDDEN.value(), "Access denied. Admin privileges required.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ResponseDTO.error("User registration failed", error.getCode(), error.getDescription()));
        }

        try {
            UserDTO registeredUser = (UserDTO) userService.register(userDTO); // Ensure userService.register() returns UserDTO
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseDTO.success("User registered successfully", registeredUser));
        } catch (IllegalArgumentException e) {
            ErrorDTO error = new ErrorDTO(HttpStatus.CONFLICT.value(), "User already exists.");
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ResponseDTO.error("Registration failed", error.getCode(), error.getDescription()));
        }
    }

    // **Forgot Password**
    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseDTO<String>> forgotPassword(@RequestBody ForgotPasswordDTO forgotPasswordDTO) {
        String response = userService.sendResetCode(forgotPasswordDTO.getEmail());
        if (response.startsWith("Reset code sent")) {
            return ResponseEntity.ok(ResponseDTO.success("Reset code sent successfully", response));
        }
        ErrorDTO error = new ErrorDTO(HttpStatus.NOT_FOUND.value(), "Email not found.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ResponseDTO.error("Password reset failed", error.getCode(), error.getDescription()));
    }

    // **Verify Reset Code**
    @PostMapping("/verify-reset-code")
    public ResponseEntity<ResponseDTO<String>> verifyResetCode(@RequestBody VerifyResetCodeDTO verifyResetCodeDTO) {
        boolean isVerified = userService.verifyResetCode(
                verifyResetCodeDTO.getEmail(),
                verifyResetCodeDTO.getResetCode(),
                verifyResetCodeDTO.getNewPassword()
        );
        if (isVerified) {
            return ResponseEntity.ok(ResponseDTO.success("Password reset successfully", "Password has been updated."));
        }
        ErrorDTO error = new ErrorDTO(HttpStatus.BAD_REQUEST.value(), "Invalid reset code.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseDTO.error("Password reset failed", error.getCode(), error.getDescription()));
    }


    // **Verify JWT**
    @PostMapping("/verify")
    public ResponseEntity<ResponseDTO<Map<String, Object>>> verifyJwt(@RequestHeader("Authorization") String authHeader) {
        if (jwtAuthenticator.validateJwtToken(authHeader)) {
            Map<String, Object> payload = jwtAuthenticator.getJwtPayload(authHeader);
            return ResponseEntity.ok(ResponseDTO.success("JWT verified successfully", payload));
        }
        ErrorDTO error = new ErrorDTO(HttpStatus.UNAUTHORIZED.value(), "Invalid or expired JWT.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ResponseDTO.error("JWT verification failed", error.getCode(), error.getDescription()));
    }

    // **Get All Users (Admin Only)**
    @GetMapping
    public ResponseEntity<ResponseDTO<List<UserDTO>>> getAllUsers(@RequestHeader("Authorization") String authHeader) {
        if (isAdmin(authHeader)) {
            List<UserDTO> users = userService.getAllUsers();
            return ResponseEntity.ok(ResponseDTO.success("Users fetched successfully", users));
        }
        ErrorDTO error = new ErrorDTO(HttpStatus.UNAUTHORIZED.value(), "Access denied. Admin privileges required.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ResponseDTO.error("Fetching users failed", error.getCode(), error.getDescription()));
    }

    // **Get User by ID**
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<UserDTO>> getUserById(@RequestHeader("Authorization") String authHeader, @PathVariable Integer id) {
        if (jwtAuthenticator.validateJwtToken(authHeader)) {
            UserDTO user = userService.getUserById(id);
            if (user != null) {
                return ResponseEntity.ok(ResponseDTO.success("User fetched successfully", user));
            }
        }
        ErrorDTO error = new ErrorDTO(HttpStatus.NOT_FOUND.value(), "User not found.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDTO.error("User retrieval failed", error.getCode(), error.getDescription()));
    }

    // **Update User**
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<UserDTO>> updateUser(@RequestHeader("Authorization") String authHeader, @PathVariable Integer id, @RequestBody UserDTO userDTO) {
        if (jwtAuthenticator.validateJwtToken(authHeader)) {
            UserDTO updatedUser = userService.updateUser(id, userDTO);
            if (updatedUser != null) {
                return ResponseEntity.ok(ResponseDTO.success("User updated successfully", updatedUser));
            }
        }
        ErrorDTO error = new ErrorDTO(HttpStatus.NOT_FOUND.value(), "User not found.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ResponseDTO.error("User update failed", error.getCode(), error.getDescription()));
    }

    // **Delete User (Admin Only)**
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> deleteUser(@RequestHeader("Authorization") String authHeader, @PathVariable Integer id) {
        if (isAdmin(authHeader)) {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        }
        ErrorDTO error = new ErrorDTO(HttpStatus.UNAUTHORIZED.value(), "Access denied. Admin privileges required.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ResponseDTO.error("User deletion failed", error.getCode(), error.getDescription()));
    }
}