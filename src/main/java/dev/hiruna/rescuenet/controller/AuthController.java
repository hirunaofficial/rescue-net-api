package dev.hiruna.rescuenet.controller;

import dev.hiruna.rescuenet.dto.AuthDTO;
import dev.hiruna.rescuenet.dto.LoginDTO;
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
    public ResponseEntity<AuthDTO> registerUser(@RequestBody AuthDTO authDTO) {
        try {
            AuthDTO registeredUser = (AuthDTO) userService.register(authDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // **Login User**
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginDTO loginDTO) {
        try {
            String jwtToken = userService.login(loginDTO.getEmail(), loginDTO.getPassword());
            return ResponseEntity.ok(jwtToken);
        } catch (AuthenticationFailedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }
}