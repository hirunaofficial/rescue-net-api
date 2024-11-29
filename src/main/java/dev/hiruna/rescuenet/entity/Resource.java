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
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String resourceType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = true)
    private String locationName;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(nullable = true)
    private String contactPerson;

    @Column(nullable = true)
    private String contactPhone;

    @Column(nullable = false)
    private LocalDateTime dateAdded;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResourceStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public enum ResourceStatus {
        AVAILABLE,
        OUT_OF_STOCK,
        UNDER_MAINTENANCE
    }

    public Resource(String resourceType, String description, int quantity, String locationName,
                    double latitude, double longitude, String contactPerson, String contactPhone,
                    LocalDateTime dateAdded, ResourceStatus resourceStatus,
                    LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.resourceType = resourceType;
        this.description = description;
        this.quantity = quantity;
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.contactPerson = contactPerson;
        this.contactPhone = contactPhone;
        this.dateAdded = dateAdded;
        this.status = resourceStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}