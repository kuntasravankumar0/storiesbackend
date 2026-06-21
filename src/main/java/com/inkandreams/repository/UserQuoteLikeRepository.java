package com.inkandreams.repository;

import com.inkandreams.entity.UserQuoteLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserQuoteLikeRepository extends JpaRepository<UserQuoteLike, Long> {
    Optional<UserQuoteLike> findByUserIdAndQuoteId(Long userId, Long quoteId);
    boolean existsByUserIdAndQuoteId(Long userId, Long quoteId);
    void deleteByUserIdAndQuoteId(Long userId, Long quoteId);
}
