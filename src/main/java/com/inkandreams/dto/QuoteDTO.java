package com.inkandreams.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class QuoteDTO {
    private Long id;
    private String text;
    private String author;
    private String category;
    private int likeCount;
    private LocalDateTime createdAt;
}
