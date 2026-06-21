package com.inkandreams.repository;

import com.inkandreams.entity.ReadingProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReadingProgressRepository extends JpaRepository<ReadingProgress, Long> {
    Optional<ReadingProgress> findByUserIdAndStoryId(Long userId, Long storyId);
    List<ReadingProgress> findByUserIdAndProgressPercentageLessThan(Long userId, int percentage);
}
