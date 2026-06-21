package com.inkandreams.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "quotes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Quote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    private String author;

    @Enumerated(EnumType.STRING)
    private Category category;

    private int likeCount;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum Category {
        LOVE, LIFE, MOTIVATION, FRIENDSHIP, SUCCESS, EMOTIONAL, INSPIRATIONAL
    }
}
