package dev.hiruna.rescuenet.controller;

import dev.hiruna.rescuenet.dto.*;
import dev.hiruna.rescuenet.exception.ResourceNotFoundException;
import dev.hiruna.rescuenet.service.ResourceService;
import dev.hiruna.rescuenet.utill.JWTAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private JWTAuthenticator jwtAuthenticator;

    // Helper method to check if the user has the "Admin" role
    private boolean isAdmin(String authHeader) {
        if (jwtAuthenticator.validateJwtToken(authHeader)) {
            Map<String, Object> payload = jwtAuthenticator.getJwtPayload(authHeader);
            if (payload != null && payload.containsKey("role")) {
                String role = (String) payload.get("role");
                return "Admin".equalsIgnoreCase(role);
            }
        }
        return false;
    }

    // **Create Resource (Admin Only)**
    @PostMapping
    public ResponseEntity<ResponseDTO<ResourceDTO>> createResource(@RequestHeader("Authorization") String authHeader, @RequestBody ResourceDTO resourceDTO) {
        if (!isAdmin(authHeader)) {
            ErrorDTO error = new ErrorDTO(HttpStatus.FORBIDDEN.value(), "Access denied. Admin privileges required.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ResponseDTO.error("Resource creation failed", error.getCode(), error.getDescription()));
        }

        try {
            ResourceDTO createdResource = resourceService.saveResource(resourceDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseDTO.success("Resource created successfully", createdResource));
        } catch (Exception e) {
            ErrorDTO error = new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Resource creation failed", error.getCode(), error.getDescription()));
        }
    }

    // **Get All Resources**
    @GetMapping
    public ResponseEntity<ResponseDTO<List<ResourceDTO>>> getAllResources() {
        List<ResourceDTO> resources = resourceService.getAllResources();
        return ResponseEntity.ok(ResponseDTO.success("Resources fetched successfully", resources));
    }

    // **Get Resource by ID**
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<ResourceDTO>> getResourceById(@PathVariable Integer id) {
        try {
            ResourceDTO resource = resourceService.getResourceById(id);
            return ResponseEntity.ok(ResponseDTO.success("Resource fetched successfully", resource));
        } catch (ResourceNotFoundException e) {
            ErrorDTO error = new ErrorDTO(HttpStatus.NOT_FOUND.value(), "Resource not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseDTO.error("Resource retrieval failed", error.getCode(), error.getDescription()));
        }
    }

    // **Update Resource (Admin Only)**
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<ResourceDTO>> updateResource(@RequestHeader("Authorization") String authHeader, @PathVariable Integer id, @RequestBody ResourceDTO resourceDTO) {
        if (!isAdmin(authHeader)) {
            ErrorDTO error = new ErrorDTO(HttpStatus.UNAUTHORIZED.value(), "Access denied. Admin privileges required.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseDTO.error("Resource update failed", error.getCode(), error.getDescription()));
        }

        try {
            ResourceDTO updatedResource = resourceService.updateResource(id, resourceDTO);
            return ResponseEntity.ok(ResponseDTO.success("Resource updated successfully", updatedResource));
        } catch (ResourceNotFoundException e) {
            ErrorDTO error = new ErrorDTO(HttpStatus.NOT_FOUND.value(), "Resource not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseDTO.error("Resource update failed", error.getCode(), error.getDescription()));
        }
    }

    // **Delete Resource (Admin Only)**
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> deleteResource(@RequestHeader("Authorization") String authHeader, @PathVariable Integer id) {
        if (!isAdmin(authHeader)) {
            ErrorDTO error = new ErrorDTO(HttpStatus.UNAUTHORIZED.value(), "Access denied. Admin privileges required.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseDTO.error("Resource deletion failed", error.getCode(), error.getDescription()));
        }

        try {
            resourceService.deleteResource(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            ErrorDTO error = new ErrorDTO(HttpStatus.NOT_FOUND.value(), "Resource not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseDTO.error("Resource deletion failed", error.getCode(), error.getDescription()));
        }
    }
}