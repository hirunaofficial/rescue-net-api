package dev.hiruna.rescuenet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String message; // The alert message

    @Column(nullable = false)
    private String type; // The type of alert (e.g., 'danger', 'info', 'warning')

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status; // Status of the alert (ACTIVE or INACTIVE)

    @Column(nullable = false)
    private LocalDateTime createdAt; // When the alert was created

    @Column(nullable = true)
    private LocalDateTime updatedAt; // When the alert was last updated

    public Alert(String message, String type, Status status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.message = message;
        this.type = type;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Alert(String message, String type, String status) {
        this.message = message;
        this.type = type;
        this.status = Status.valueOf(status);
    }

    // Enum for alert status
    public enum Status {
        ACTIVE, INACTIVE;
    }

    // Constructor for alert without the createdAt and updatedAt fields
    public Alert(String message, String type, Status status) {
        this.message = message;
        this.type = type;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Lifecycle hook to automatically set createdAt before persistence
    @PrePersist
    public void onPrePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Lifecycle hook to automatically update updatedAt before update
    @PreUpdate
    public void onPreUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}