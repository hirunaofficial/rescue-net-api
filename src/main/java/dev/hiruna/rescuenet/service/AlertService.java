package dev.hiruna.rescuenet.service;

import dev.hiruna.rescuenet.dto.AlertDTO;
import dev.hiruna.rescuenet.entity.Alert;
import dev.hiruna.rescuenet.exception.AlertNotFoundException;
import dev.hiruna.rescuenet.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlertService {

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;  // Template for sending messages to WebSocket clients

    // **Create Alert**
    public AlertDTO saveAlert(AlertDTO alertDTO) {
        // Validate the DTO data
        if (alertDTO.getMessage() == null || alertDTO.getMessage().isEmpty()) {
            throw new IllegalArgumentException("Alert message cannot be null or empty");
        }

        Alert alert = new Alert(alertDTO.getMessage(), alertDTO.getType(), alertDTO.getStatus());
        alert = alertRepository.save(alert);

        // After creating the alert, push all alerts to WebSocket clients
        broadcastAlerts();
        return new AlertDTO(alert);
    }

    // **Get All Alerts**
    public List<AlertDTO> getAllAlerts() {
        return alertRepository.findAll().stream()
                .map(AlertDTO::new)  // Convert each Alert to AlertDTO
                .collect(Collectors.toList());
    }

    // **Update Alert**
    public AlertDTO updateAlert(Integer id, AlertDTO alertDTO) {
        Alert alert = alertRepository.findById(id).orElseThrow(() -> new AlertNotFoundException("Alert not found"));

        // Update the alert fields with values from the DTO
        alert.setMessage(alertDTO.getMessage());
        alert.setType(alertDTO.getType());
        alert.setStatus(Alert.Status.valueOf(alertDTO.getStatus()));  // Ensure status is valid

        alert = alertRepository.save(alert);

        // After updating the alert, push all alerts to WebSocket clients
        broadcastAlerts();
        return new AlertDTO(alert);
    }

    // **Delete Alert**
    public void deleteAlert(Integer id) {
        Alert alert = alertRepository.findById(id).orElseThrow(() -> new AlertNotFoundException("Alert not found"));
        alertRepository.delete(alert);

        // After deleting the alert, push all alerts to WebSocket clients
        broadcastAlerts();
    }

    // **Broadcast All Alerts to WebSocket clients**
    public void broadcastAlerts() {
        List<AlertDTO> allAlerts = getAllAlerts();  // Get the latest list of alerts
        messagingTemplate.convertAndSend("/topic/alerts", allAlerts);  // Send the updated list of alerts to WebSocket clients
    }
}