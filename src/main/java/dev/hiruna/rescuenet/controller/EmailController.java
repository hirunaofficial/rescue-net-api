package dev.hiruna.rescuenet.controller;

import dev.hiruna.rescuenet.dto.EmailDTO;
import dev.hiruna.rescuenet.dto.ResponseDTO;
import dev.hiruna.rescuenet.dto.ErrorDTO;
import dev.hiruna.rescuenet.utill.EmailSender;
import dev.hiruna.rescuenet.utill.JWTAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private JWTAuthenticator jwtAuthenticator;

    // Helper method to validate JWT
    private boolean isTokenValid(String authHeader) {
        return jwtAuthenticator.validateJwtToken(authHeader);
    }

    // Endpoint to send an email
    @PostMapping("/send")
    public ResponseEntity<ResponseDTO<String>> sendEmail(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody EmailDTO emailDTO
    ) {
        // Verify the JWT token
        if (!isTokenValid(authHeader)) {
            ErrorDTO error = new ErrorDTO(HttpStatus.UNAUTHORIZED.value(), "Invalid or expired JWT.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseDTO.error("Email sending failed", error.getCode(), error.getDescription()));
        }

        try {
            // Extract user info from the JWT payload (optional)
            Map<String, Object> payload = jwtAuthenticator.getJwtPayload(authHeader);
            String sender = (String) payload.getOrDefault("email", "Unknown user");

            // Send email
            emailSender.sendEmail(emailDTO.getTo(), emailDTO.getSubject(), emailDTO.getBody());
            return ResponseEntity.ok(ResponseDTO.success("Email sent successfully to " + emailDTO.getTo(), "Sent by: " + sender));
        } catch (Exception e) {
            // Create an ErrorDTO for the exception
            ErrorDTO error = new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to send email: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Email sending failed", error.getCode(), error.getDescription()));
        }
    }
}