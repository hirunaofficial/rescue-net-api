package dev.hiruna.rescuenet.controller;

import dev.hiruna.rescuenet.dto.EmailDTO;
import dev.hiruna.rescuenet.utill.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailSender emailSender;

    // Endpoint to send an email
    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailDTO emailDTO) {
        try {
            emailSender.sendEmail(emailDTO.getTo(), emailDTO.getSubject(), emailDTO.getBody());
            return ResponseEntity.ok("Email sent successfully to " + emailDTO.getTo());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email: " + e.getMessage());
        }
    }
}