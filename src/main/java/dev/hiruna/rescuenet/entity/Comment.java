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
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private LocalDateTime publishDate;

    // Many-to-one relationship with User (user who commented)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Many-to-one relationship with Post (post to which the comment belongs)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    // Constructor to make it easier to create a new comment
    public Comment(String content, LocalDateTime publishDate, User user, Post post) {
        this.content = content;
        this.publishDate = publishDate;
        this.user = user;
        this.post = post;
    }

    // Constructor using userId and postId (fetch User and Post by ID)
    public Comment(String content, LocalDateTime publishDate, Integer userId, Integer postId) {
        this.content = content;
        this.publishDate = publishDate;
        this.user = new User();
        this.user.setId(userId);
        this.post = new Post();
        this.post.setId(postId);
    }

    public Integer getPostId() {
        return post.getId();
    }

    public Integer getUserId() {
        return user.getId();
    }
}