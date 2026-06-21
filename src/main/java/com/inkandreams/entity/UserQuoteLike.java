package com.inkandreams.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_quote_likes", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "quote_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserQuoteLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "quote_id", nullable = false)
    private Long quoteId;
}
