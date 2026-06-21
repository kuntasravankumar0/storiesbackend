package com.inkandreams.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentDTO {
    private Long id;
    private String content;
    private String username;
    private String userProfilePicture;
    private Long storyId;
    private String storyTitle;
    private boolean approved;
    private LocalDateTime createdAt;
}
