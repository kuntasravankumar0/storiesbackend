package com.inkandreams.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AudioStoryCreateRequest {
    @NotBlank
    private String title;

    private String coverImageUrl;

    @NotBlank
    private String audioUrl;

    private String author;
    private String genre;
    private String language;
    private int durationSeconds;
    private boolean downloadable;
}
