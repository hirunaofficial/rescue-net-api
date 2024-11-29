package dev.hiruna.rescuenet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsDTO {

    private Integer id;
    private String title;
    private String content;
    private LocalDateTime publishDate;
    private String imageUrl;
    private String tags;

    public String getFullTitle() {
        return title;
    }
}