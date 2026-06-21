package com.inkandreams.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

@Data
public class StoryCreateRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private String coverImageUrl;
    private String author;
    private String genre;
    private String language;
    private int readingTimeMinutes;
    private boolean featured;
    private String authorNote;
    private List<String> inlineImageUrls;
}
