package com.inkandreams.repository;

import com.inkandreams.entity.UserStoryLove;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserStoryLoveRepository extends JpaRepository<UserStoryLove, Long> {
    Optional<UserStoryLove> findByUserIdAndStoryId(Long userId, Long storyId);
    boolean existsByUserIdAndStoryId(Long userId, Long storyId);
    void deleteByUserIdAndStoryId(Long userId, Long storyId);
    List<UserStoryLove> findByUserId(Long userId);
}
