package com.inkandreams.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "writer_profile")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class WriterProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String photoUrl;

    @Column(columnDefinition = "TEXT")
    private String biography;

    @Column(columnDefinition = "TEXT")
    private String writingJourney;

    @Column(columnDefinition = "TEXT")
    private String achievements;

    private int numberOfStoriesWritten;

    private String socialMediaLinks; // JSON string

    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
