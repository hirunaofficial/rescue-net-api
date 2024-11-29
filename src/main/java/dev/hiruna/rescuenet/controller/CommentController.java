package dev.hiruna.rescuenet.controller;

import dev.hiruna.rescuenet.dto.CommentDTO;
import dev.hiruna.rescuenet.dto.ResponseDTO;
import dev.hiruna.rescuenet.exception.CommentNotFoundException;
import dev.hiruna.rescuenet.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // **Create or Update Comment** - Handles both Create and Update logic (id is optional)
    @PostMapping
    public ResponseEntity<ResponseDTO<CommentDTO>> saveComment(@RequestBody CommentDTO commentDTO) {
        try {
            CommentDTO savedComment = commentService.saveComment(commentDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDTO.success("Comment saved successfully", savedComment));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO.error("Failed to save comment", 400, e.getMessage()));
        }
    }

    // **Get All Comments**
    @GetMapping
    public ResponseEntity<ResponseDTO<List<CommentDTO>>> getAllComments() {
        List<CommentDTO> comments = commentService.getAllComments();
        return ResponseEntity.ok(ResponseDTO.success("Comments retrieved successfully", comments));
    }

    // **Get Comment by ID**
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<CommentDTO>> getCommentById(@PathVariable Integer id) {
        try {
            CommentDTO comment = commentService.getCommentById(id);
            return ResponseEntity.ok(ResponseDTO.success("Comment retrieved successfully", comment));
        } catch (CommentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDTO.error("Comment not found", 404, e.getMessage()));
        }
    }

    // **Get Comments by Post ID**
    @GetMapping("/post/{postId}")
    public ResponseEntity<ResponseDTO<List<CommentDTO>>> getCommentsByPostId(@PathVariable Integer postId) {
        try {
            List<CommentDTO> comments = commentService.getCommentsByPostId(postId);
            return ResponseEntity.ok(ResponseDTO.success("Comments retrieved successfully", comments));
        } catch (CommentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDTO.error("No comments found for Post ID " + postId, 404, e.getMessage()));
        }
    }

    // **Delete Comment by ID**
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> deleteComment(@PathVariable Integer id) {
        try {
            commentService.deleteComment(id);
            return ResponseEntity.ok(ResponseDTO.success("Comment deleted successfully", "Comment ID: " + id));
        } catch (CommentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDTO.error("Comment not found", 404, e.getMessage()));
        }
    }

    // **Update Comment by ID**
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<CommentDTO>> updateComment(
            @PathVariable Integer id,
            @RequestBody CommentDTO commentDTO) {
        try {
            CommentDTO updatedComment = commentService.updateComment(id, commentDTO);
            return ResponseEntity.ok(ResponseDTO.success("Comment updated successfully", updatedComment));
        } catch (CommentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDTO.error("Comment not found", 404, e.getMessage()));
        }
    }
}