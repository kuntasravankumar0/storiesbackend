package com.inkandreams.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stories")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Story {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String coverImageUrl;
    
    private String author;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    @Enumerated(EnumType.STRING)
    private Language language;

    private int readingTimeMinutes;
    private int loveCount;
    private int viewCount;
    private double averageRating;
    private int ratingCount;
    private boolean featured;
    private boolean approved = true; // Admin stories are auto-approved, user stories need approval
    private Long createdByUserId; // null = admin created, non-null = user created

    @Column(columnDefinition = "TEXT")
    private String authorNote;

    @Column(columnDefinition = "LONGTEXT")
    private String inlineImageUrls;

    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum Genre {
        LOVE, HORROR, THRILLER, COMEDY, DRAMA, FANTASY, MYSTERY, ADVENTURE, SCIFI, MOTIVATION, FRIENDSHIP, EMOTIONAL, ACTION
    }

    public enum Language {
        ENGLISH, TELUGU, HINDI
    }
}
