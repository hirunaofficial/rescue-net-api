package dev.hiruna.rescuenet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    private Integer id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'User'")
    private String role;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = true)
    private String resetCode;

    @Column(nullable = true)
    private String profileImageUrl; // URL to profile image

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(20) DEFAULT 'ACTIVE'")
    private AccountStatus accountStatus = AccountStatus.ACTIVE; // Account status (ACTIVE, INACTIVE, etc.)

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // Track when the account was created

    // Constructor without `id` field (to be used during registration)
    public User(String email, String password, String phoneNumber, String role, String firstName, String lastName , String profileImageUrl) {
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImageUrl = profileImageUrl;
    }

    public User(Integer userId) {
        this.id = userId;
    }

    // Helper method to return full name of the user
    public String getFullName() {
        return firstName + " " + lastName;
    }

    // Enum for account status
    public enum AccountStatus {
        ACTIVE,
        INACTIVE,
        SUSPENDED
    }
}