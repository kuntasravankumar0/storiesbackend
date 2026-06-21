package com.inkandreams.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "social_media_links")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SocialMediaLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String platform; // Facebook, Twitter, Instagram, etc.

    @Column(nullable = false)
    private String url;

    private String iconUrl;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
