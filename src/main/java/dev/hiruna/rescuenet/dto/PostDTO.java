package dev.hiruna.rescuenet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {

    private Integer id;
    private String title;
    private String content;
    private LocalDateTime publishDate;
    private String imageUrl;
    private Integer user_id;

    // A method to retrieve the full title (you could modify this if you want to add any additional formatting in the future)
    public String getFullTitle() {
        return title != null ? title : "No title provided";
    }

    // Optionally, you could add a method to get a short description (if needed in your application)
    public String getShortContent() {
        if (content != null && content.length() > 100) {
            return content.substring(0, 100) + "...";
        }
        return content;
    }
}