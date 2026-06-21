package com.inkandreams.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_story_loves", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "story_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserStoryLove {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "story_id", nullable = false)
    private Long storyId;
}
