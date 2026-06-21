package com.inkandreams.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class StoryDTO {
    private Long id;
    private String title;
    private String content;
    private String coverImageUrl;
    private String author;
    private String genre;
    private String language;
    private int readingTimeMinutes;
    private int loveCount;
    private int viewCount;
    private double averageRating;
    private int ratingCount;
    private boolean featured;
    private String authorNote;
    private List<String> inlineImageUrls;
    private LocalDateTime createdAt;
    private int commentCount;

    public void setInlineImageUrlsFromString(String urls) {
        if (urls != null && !urls.isEmpty()) {
            this.inlineImageUrls = java.util.Arrays.asList(urls.split(","));
        } else {
            this.inlineImageUrls = new java.util.ArrayList<>();
        }
    }
}
