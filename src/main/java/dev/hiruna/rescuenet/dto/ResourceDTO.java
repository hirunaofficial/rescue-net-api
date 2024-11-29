package dev.hiruna.rescuenet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceDTO {

    private Integer id;
    private String resourceType;
    private String description;
    private int quantity;
    private String locationName;
    private double latitude;
    private double longitude;
    private String contactPerson;
    private String contactPhone;
    private LocalDateTime dateAdded;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public String getFullLocation() {
        return locationName + " (" + latitude + ", " + longitude + ")";
    }

    public String getFormattedStatus() {
        return status != null ? status.toUpperCase() : "UNKNOWN";
    }
}