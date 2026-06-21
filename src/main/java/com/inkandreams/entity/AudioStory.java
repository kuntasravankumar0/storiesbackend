package com.inkandreams.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audio_stories")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AudioStory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String coverImageUrl;
    private String audioUrl;
    private String author;

    @Enumerated(EnumType.STRING)
    private Story.Genre genre;

    @Enumerated(EnumType.STRING)
    private Story.Language language;

    private int durationSeconds;
    private int playCount;
    private boolean downloadable;
    private boolean approved = true;
    private Long createdByUserId;

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
}
