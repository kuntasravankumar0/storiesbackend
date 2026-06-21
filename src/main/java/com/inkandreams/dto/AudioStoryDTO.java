package com.inkandreams.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AudioStoryDTO {
    private Long id;
    private String title;
    private String coverImageUrl;
    private String audioUrl;
    private String author;
    private String genre;
    private String language;
    private int durationSeconds;
    private int playCount;
    private boolean downloadable;
    private LocalDateTime createdAt;
}
