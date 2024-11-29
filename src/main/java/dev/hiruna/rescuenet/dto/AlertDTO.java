package dev.hiruna.rescuenet.dto;

import dev.hiruna.rescuenet.entity.Alert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertDTO {

    private Integer id;
    private String message;
    private String type;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor to easily map from an Alert entity
    public AlertDTO(Alert alert) {
        this.id = alert.getId();
        this.message = alert.getMessage();
        this.type = alert.getType();
        this.status = alert.getStatus().name();
        this.createdAt = alert.getCreatedAt();
        this.updatedAt = alert.getUpdatedAt();
    }

    // Convert DTO back to entity if needed
    public Alert toEntity() {
        Alert alert = new Alert();
        alert.setId(this.id);
        alert.setMessage(this.message);
        alert.setType(this.type);
        alert.setStatus(Alert.Status.valueOf(this.status));
        alert.setCreatedAt(this.createdAt);
        alert.setUpdatedAt(this.updatedAt);
        return alert;
    }
}
