package dev.hiruna.rescuenet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    private Integer id;
    private String content;
    private LocalDateTime publishDate;
    private UserDTO user;
    private Integer postId;

    public CommentDTO(Integer id, String content, LocalDateTime publishDate, Integer userId, Integer postId) {
        this.id = id;
        this.content = content;
        this.publishDate = publishDate;
        this.user = new UserDTO();
        this.user.setId(userId);
        this.postId = postId;
    }

    public Integer getUserId() {
        return user.getId();
    }

}
