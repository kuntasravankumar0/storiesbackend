package com.inkandreams.repository;

import com.inkandreams.entity.UserStoryBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserStoryBookmarkRepository extends JpaRepository<UserStoryBookmark, Long> {
    Optional<UserStoryBookmark> findByUserIdAndStoryId(Long userId, Long storyId);
    boolean existsByUserIdAndStoryId(Long userId, Long storyId);
    void deleteByUserIdAndStoryId(Long userId, Long storyId);
    List<UserStoryBookmark> findByUserId(Long userId);
}
