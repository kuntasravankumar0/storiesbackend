package com.inkandreams.repository;

import com.inkandreams.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByUserIdAndStoryId(Long userId, Long storyId);
    
    @Query("SELECT AVG(r.value) FROM Rating r WHERE r.story.id = :storyId")
    Double findAverageRatingByStoryId(@Param("storyId") Long storyId);
    
    long countByStoryId(Long storyId);
}
