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
public class News {

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

    @Column(nullable = true)
    private String tags;

    public News(String title, String content, LocalDateTime publishDate, String imageUrl, String tags) {
        this.title = title;
        this.content = content;
        this.publishDate = publishDate;
        this.imageUrl = imageUrl;
        this.tags = tags;
    }

    public String[] getTagsArray() {
        if (tags != null && !tags.isEmpty()) {
            return tags.split(",");
        }
        return new String[0];
    }

    public void setTagsFromArray(String[] tagsArray) {
        if (tagsArray != null && tagsArray.length > 0) {
            this.tags = String.join(",", tagsArray);
        }
    }
}