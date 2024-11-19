package dev.hiruna.rescuenet.controller;

import dev.hiruna.rescuenet.dto.AuthDTO;
import dev.hiruna.rescuenet.dto.LoginDTO;
import dev.hiruna.rescuenet.dto.ResponseDTO;
import dev.hiruna.rescuenet.dto.ErrorDTO;
import dev.hiruna.rescuenet.service.UserService;
import dev.hiruna.rescuenet.exception.UserAlreadyExistsException;
import dev.hiruna.rescuenet.exception.AuthenticationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // **Register User**
    @PostMapping("/register")
    public ResponseEntity<ResponseDTO<AuthDTO>> registerUser(@RequestBody AuthDTO authDTO) {
        try {
            AuthDTO registeredUser = (AuthDTO) userService.register(authDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseDTO.success("User registered successfully", registeredUser));
        } catch (UserAlreadyExistsException e) {
            ErrorDTO error = new ErrorDTO(HttpStatus.CONFLICT.value(), "Email already exists.");
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ResponseDTO.error("Registration failed", error.getCode(), error.getDescription()));
        } catch (Exception e) {
            ErrorDTO error = new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Registration failed", error.getCode(), error.getDescription()));
        }
    }

    // **Login User**
    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<String>> loginUser(@RequestBody LoginDTO loginDTO) {
        try {
            String jwtToken = userService.login(loginDTO.getEmail(), loginDTO.getPassword());
            return ResponseEntity.ok(ResponseDTO.success("Login successful", jwtToken));
        } catch (AuthenticationFailedException e) {
            ErrorDTO error = new ErrorDTO(HttpStatus.UNAUTHORIZED.value(), "Invalid credentials.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseDTO.error("Login failed", error.getCode(), error.getDescription()));
        } catch (Exception e) {
            ErrorDTO error = new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Login failed", error.getCode(), error.getDescription()));
        }
    }
}