package dev.hiruna.rescuenet.controller;

import dev.hiruna.rescuenet.dto.*;
import dev.hiruna.rescuenet.exception.NewsNotFoundException;
import dev.hiruna.rescuenet.service.NewsService;
import dev.hiruna.rescuenet.utill.JWTAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

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

    // **Create News Article (Admin Only)**
    @PostMapping
    public ResponseEntity<ResponseDTO<NewsDTO>> createNews(@RequestHeader("Authorization") String authHeader, @RequestBody NewsDTO newsDTO) {
        if (!isAdmin(authHeader)) {
            ErrorDTO error = new ErrorDTO(HttpStatus.FORBIDDEN.value(), "Access denied. Admin privileges required.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ResponseDTO.error("News creation failed", error.getCode(), error.getDescription()));
        }

        try {
            NewsDTO createdNews = newsService.saveNews(newsDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseDTO.success("News created successfully", createdNews));
        } catch (Exception e) {
            ErrorDTO error = new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("News creation failed", error.getCode(), error.getDescription()));
        }
    }

    // **Get All News Articles**
    @GetMapping
    public ResponseEntity<ResponseDTO<List<NewsDTO>>> getAllNews() {
        List<NewsDTO> newsList = newsService.getAllNews();
        return ResponseEntity.ok(ResponseDTO.success("News fetched successfully", newsList));
    }

    // **Get News Article by ID**
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<NewsDTO>> getNewsById(@PathVariable Integer id) {
        try {
            NewsDTO news = newsService.getNewsById(id);
            return ResponseEntity.ok(ResponseDTO.success("News fetched successfully", news));
        } catch (NewsNotFoundException e) {
            ErrorDTO error = new ErrorDTO(HttpStatus.NOT_FOUND.value(), "News article not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseDTO.error("News retrieval failed", error.getCode(), error.getDescription()));
        }
    }

    // **Update News Article (Admin Only)**
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<NewsDTO>> updateNews(@RequestHeader("Authorization") String authHeader, @PathVariable Integer id, @RequestBody NewsDTO newsDTO) {
        if (!isAdmin(authHeader)) {
            ErrorDTO error = new ErrorDTO(HttpStatus.UNAUTHORIZED.value(), "Access denied. Admin privileges required.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseDTO.error("News update failed", error.getCode(), error.getDescription()));
        }

        try {
            NewsDTO updatedNews = newsService.updateNews(id, newsDTO);
            return ResponseEntity.ok(ResponseDTO.success("News updated successfully", updatedNews));
        } catch (NewsNotFoundException e) {
            ErrorDTO error = new ErrorDTO(HttpStatus.NOT_FOUND.value(), "News article not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseDTO.error("News update failed", error.getCode(), error.getDescription()));
        }
    }

    // **Delete News Article (Admin Only)**
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> deleteNews(@RequestHeader("Authorization") String authHeader, @PathVariable Integer id) {
        if (!isAdmin(authHeader)) {
            ErrorDTO error = new ErrorDTO(HttpStatus.UNAUTHORIZED.value(), "Access denied. Admin privileges required.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseDTO.error("News deletion failed", error.getCode(), error.getDescription()));
        }

        try {
            newsService.deleteNews(id);
            return ResponseEntity.noContent().build();
        } catch (NewsNotFoundException e) {
            ErrorDTO error = new ErrorDTO(HttpStatus.NOT_FOUND.value(), "News article not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseDTO.error("News deletion failed", error.getCode(), error.getDescription()));
        }
    }
}