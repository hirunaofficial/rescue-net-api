package dev.hiruna.rescuenet.controller;

import dev.hiruna.rescuenet.dto.AlertDTO;
import dev.hiruna.rescuenet.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertWebSocketController {

    @Autowired
    private AlertService alertService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;  // For sending WebSocket messages to clients

    /**
     * Broadcasts the updated list of alerts to all clients subscribed to /topic/alerts.
     * This method will be triggered after creating, updating, or deleting an alert.
     */
    private void broadcastAlerts() {
        List<AlertDTO> alerts = alertService.getAllAlerts();
        messagingTemplate.convertAndSend("/topic/alerts", alerts); // Send updates to all clients
    }

    /**
     * This method listens for incoming alert updates from clients (optional).
     * Whenever a client sends a message to "/app/alert", the alerts are broadcasted
     * to all subscribers of "/topic/alerts".
     */
    @MessageMapping("/alert")
    @SendTo("/topic/alerts")
    public List<AlertDTO> sendAlerts() {
        return alertService.getAllAlerts();
    }

    /**
     * This method will be used to fetch all alerts initially when a page is loaded or refreshed.
     * It provides the alerts through a regular HTTP GET request.
     */
    @GetMapping
    public List<AlertDTO> getAllAlerts() {
        return alertService.getAllAlerts();
    }

    /**
     * This method will handle updating alerts and sending real-time updates to clients.
     */
    @PutMapping("/{id}")
    public AlertDTO updateAlert(@PathVariable Integer id, @RequestBody AlertDTO alertDTO) {
        AlertDTO updatedAlert = alertService.updateAlert(id, alertDTO);
        broadcastAlerts();  // Notify WebSocket clients of the update
        return updatedAlert;
    }

    /**
     * This method will handle creating new alerts and sending real-time updates to clients.
     */
    @PostMapping
    public AlertDTO createAlert(@RequestBody AlertDTO alertDTO) {
        AlertDTO createdAlert = alertService.saveAlert(alertDTO);
        broadcastAlerts();  // Notify WebSocket clients of the new alert
        return createdAlert;
    }

    /**
     * This method will handle deleting alerts and sending real-time updates to clients.
     */
    @DeleteMapping("/{id}")
    public void deleteAlert(@PathVariable Integer id) {
        alertService.deleteAlert(id);
        broadcastAlerts();  // Notify WebSocket clients of the deleted alert
    }
}