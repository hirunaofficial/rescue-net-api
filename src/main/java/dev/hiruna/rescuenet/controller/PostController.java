package dev.hiruna.rescuenet.controller;

import dev.hiruna.rescuenet.dto.PostDTO;
import dev.hiruna.rescuenet.dto.ResponseDTO;
import dev.hiruna.rescuenet.exception.PostNotFoundException;
import dev.hiruna.rescuenet.service.PostService;
import dev.hiruna.rescuenet.utill.JWTAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private JWTAuthenticator jwtAuthenticator;

    // **Helper method to check if the user has the "Admin" role**
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

    // **Create or Update Post (Authenticated)**
    @PostMapping
    public ResponseEntity<ResponseDTO<PostDTO>> savePost(@RequestHeader("Authorization") String authHeader, @RequestBody PostDTO postDTO) {
        try {
            // Check if the user is authenticated
            if (!jwtAuthenticator.validateJwtToken(authHeader)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ResponseDTO.error("Authentication failed", 401, "Invalid or expired JWT."));
            }

            // If the JWT is valid, proceed to create or update the post
            PostDTO savedPost = postService.savePost(postDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDTO.success("Post created successfully", savedPost));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO.error("Failed to create post", 400, e.getMessage()));
        }
    }

    // **Get All Posts**
    @GetMapping
    public ResponseEntity<ResponseDTO<List<PostDTO>>> getAllPosts() {
        List<PostDTO> posts = postService.getAllPosts();
        return ResponseEntity.ok(ResponseDTO.success("Posts retrieved successfully", posts));
    }

    // **Get Post by ID**
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<PostDTO>> getPostById(@PathVariable Integer id) {
        try {
            PostDTO post = postService.getPostById(id);
            return ResponseEntity.ok(ResponseDTO.success("Post retrieved successfully", post));
        } catch (PostNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDTO.error("Post not found", 404, e.getMessage()));
        }
    }

    // **Delete Post by ID (Admin Only)**
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> deletePost(@RequestHeader("Authorization") String authHeader, @PathVariable Integer id) {
        // Check if the user has Admin privileges
        if (!isAdmin(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseDTO.error("Post deletion failed", 401, "Access denied. Admin privileges required."));
        }

        try {
            postService.deletePost(id);
            return ResponseEntity.ok(ResponseDTO.success("Post deleted successfully", "Post ID: " + id));
        } catch (PostNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDTO.error("Post not found", 404, e.getMessage()));
        }
    }

    // **Update Post by ID (Admin Only)**
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<PostDTO>> updatePost(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer id,
            @RequestBody PostDTO postDTO) {

        // Check if the user has Admin privileges
        if (!isAdmin(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseDTO.error("Post update failed", 401, "Access denied. Admin privileges required."));
        }

        try {
            PostDTO updatedPost = postService.updatePost(id, postDTO);
            return ResponseEntity.ok(ResponseDTO.success("Post updated successfully", updatedPost));
        } catch (PostNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDTO.error("Post not found", 404, e.getMessage()));
        }
    }

    // **Get Posts by User ID**
    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseDTO<List<PostDTO>>> getPostsByUserId(@PathVariable Integer userId) {
        try {
            List<PostDTO> posts = postService.getPostsByUserId(userId);
            return ResponseEntity.ok(ResponseDTO.success("Posts retrieved successfully", posts));
        } catch (PostNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseDTO.error("Posts not found", 404, e.getMessage()));
        }
    }
}