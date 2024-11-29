package dev.hiruna.rescuenet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private LocalDateTime publishDate;

    @Column(nullable = true)
    private String imageUrl;

    // Many-to-one relationship with User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // One-to-many relationship with comments
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;

    // Constructor without comments field for simplicity
    public Post(String title, String content, LocalDateTime publishDate, String imageUrl, User user) {
        this.title = title;
        this.content = content;
        this.publishDate = publishDate;
        this.imageUrl = imageUrl;
        this.user = user;
    }

    public Post(String title, String content, LocalDateTime publishDate, String imageUrl) {
        this.title = title;
        this.content = content;
        this.publishDate = publishDate;
        this.imageUrl = imageUrl;
    }

    public Post(String title, String content, LocalDateTime publishDate, String imageUrl, Integer userId) {
        this.title = title;
        this.content = content;
        this.publishDate = publishDate;
        this.imageUrl = imageUrl;
        this.user = new User(userId);
    }
}