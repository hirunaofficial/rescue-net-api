package dev.hiruna.rescuenet.service;

import dev.hiruna.rescuenet.dto.UserDTO;
import dev.hiruna.rescuenet.entity.User;
import dev.hiruna.rescuenet.repository.UserRepository;
import dev.hiruna.rescuenet.utill.EmailSender;
import dev.hiruna.rescuenet.utill.JWTAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private JWTAuthenticator jwtAuthenticator;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final Random random = new Random();

    // **Register User**
    public UserDTO register(UserDTO userDTO) {
        // Check if the email already exists
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new IllegalArgumentException("Email is already registered.");
        }

        // Create user entity
        User user = convertToEntity(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword())); // Hash password
        user = userRepository.save(user);

        // Send welcome email
        sendWelcomeEmail(user.getEmail(), user.getFirstName());

        return convertToDTOWithoutPassword(user);
    }

    // **Login User**
    public String login(String email, String password) {
        // Find user by email
        User user = userRepository.findByEmail(email);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password.");
        }

        // Generate JWT token
        return jwtAuthenticator.generateJwtToken(user);
    }

    public void sendWelcomeEmail(String email, String firstName) {
        String subject = "Welcome to RescueNet!";
        String body = "<h1>Welcome, " + firstName + "!</h1>"
                + "<p>Thank you for joining RescueNet. We're here to support you in emergencies and beyond.</p>"
                + "<p>If you have any questions, feel free to contact us.</p>";

        emailSender.sendEmail(email, subject, body);
    }

    public String sendResetCode(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            String resetCode = String.format("%06d", random.nextInt(999999)); // Generate a 6-digit reset code
            user.setPassword(resetCode); // Temporarily store reset code as the password
            userRepository.save(user);

            // Send reset code email
            String subject = "Password Reset Code";
            String body = "<p>Your password reset code is: <strong>" + resetCode + "</strong></p>"
                    + "<p>Please use this code to reset your password. If you did not request this, please ignore this email.</p>";

            emailSender.sendEmail(email, subject, body);

            return "Reset code sent to " + email;
        }
        return "Email not found.";
    }

    public boolean verifyResetCode(String email, String resetCode, String newPassword) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(resetCode)) {
            user.setPassword(passwordEncoder.encode(newPassword)); // Update password with hashed version
            userRepository.save(user);

            // Send confirmation email
            String subject = "Password Reset Successful";
            String body = "<p>Your password has been successfully reset. You can now log in with your new password.</p>";

            emailSender.sendEmail(email, subject, body);

            return true;
        }
        return false;
    }

    public UserDTO updateUser(Integer id, UserDTO userDTO) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setEmail(userDTO.getEmail());
            user.setPhoneNumber(userDTO.getPhoneNumber());
            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userDTO.getPassword())); // Update hashed password
            }
            user.setRole(userDTO.getRole());
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user = userRepository.save(user);
            return convertToDTOWithoutPassword(user);
        }
        return null;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTOWithoutPassword)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Integer id) {
        User user = userRepository.findById(id).orElse(null);
        return user != null ? convertToDTOWithoutPassword(user) : null;
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    private UserDTO convertToDTOWithoutPassword(User user) {
        return new UserDTO(user.getId(), user.getEmail(), user.getPhoneNumber(), null, user.getRole(), user.getFirstName(), user.getLastName());
    }

    private User convertToEntity(UserDTO userDTO) {
        return new User(userDTO.getEmail(), userDTO.getPassword(), userDTO.getPhoneNumber(), userDTO.getRole(), userDTO.getFirstName(), userDTO.getLastName());
    }
}