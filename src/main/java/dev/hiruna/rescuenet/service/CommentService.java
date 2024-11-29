package dev.hiruna.rescuenet.service;

import dev.hiruna.rescuenet.entity.Comment;
import dev.hiruna.rescuenet.entity.Post;
import dev.hiruna.rescuenet.exception.CommentNotFoundException;
import dev.hiruna.rescuenet.repository.PostRepository;
import dev.hiruna.rescuenet.dto.CommentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private dev.hiruna.rescuenet.repository.CommentRepository CommentRepository;

    @Autowired
    private PostRepository postRepository;

    // **Create or Update Comment**
    public CommentDTO saveComment(CommentDTO commentDTO) {
        Comment comment = convertToEntity(commentDTO);

        // Set the publish date if not present
        if (comment.getPublishDate() == null) {
            comment.setPublishDate(LocalDateTime.now());
        }

        // Validate the post exists
        Optional<Post> post = postRepository.findById(comment.getPostId());
        if (post.isEmpty()) {
            throw new IllegalArgumentException("Post with ID " + comment.getPostId() + " not found");
        }

        comment = CommentRepository.save(comment);

        return convertToDTO(comment);
    }

    // **Get All Comments**
    public List<CommentDTO> getAllComments() {
        return CommentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }



    // **Get Comment by ID**
    public CommentDTO getCommentById(Integer id) {
        Comment comment = CommentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment with ID " + id + " not found"));

        return convertToDTO(comment);
    }

    // **Delete Comment by ID**
    public void deleteComment(Integer id) {
        Optional<Comment> comment = CommentRepository.findById(id);
        if (comment.isPresent()) {
            CommentRepository.deleteById(id);
        } else {
            throw new CommentNotFoundException("Comment with ID " + id + " not found");
        }
    }

    // **Update Comment**
    public CommentDTO updateComment(Integer id, CommentDTO commentDTO) {
        Comment existingComment = CommentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment with ID " + id + " not found"));

        existingComment.setContent(commentDTO.getContent());
        existingComment.setPublishDate(LocalDateTime.now());  // Optionally update publish date on edit

        existingComment = CommentRepository.save(existingComment);
        return convertToDTO(existingComment);
    }

    // **Get Comments by Post ID**
    public List<CommentDTO> getCommentsByPostId(Integer postId) {
        // Fetch the Post entity by its ID
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post with ID " + postId + " not found"));

        // Fetch the comments associated with the Post
        List<Comment> comments = CommentRepository.findByPost(post);  // Use findByPost method
        if (comments.isEmpty()) {
            throw new CommentNotFoundException("No comments found for Post ID " + postId);
        }

        // Convert the comments to DTOs and return
        return comments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // **Convert Comment Entity to CommentDTO**
    private CommentDTO convertToDTO(Comment comment) {
        return new CommentDTO(
                comment.getId(),
                comment.getContent(),
                comment.getPublishDate(),
                comment.getUserId(),
                comment.getPostId()
        );
    }

    // **Convert CommentDTO to Comment Entity**
    private Comment convertToEntity(CommentDTO commentDTO) {
        return new Comment(
                commentDTO.getContent(),
                commentDTO.getPublishDate(),
                commentDTO.getUserId(),
                commentDTO.getPostId()
        );
    }
}