package com.inkandreams.repository;

import com.inkandreams.entity.Story;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {
    Page<Story> findByApprovedTrue(Pageable pageable);
    Page<Story> findByGenreAndApprovedTrue(Story.Genre genre, Pageable pageable);
    Page<Story> findByLanguageAndApprovedTrue(Story.Language language, Pageable pageable);
    Page<Story> findByReadingTimeMinutesLessThanEqualAndApprovedTrue(int minutes, Pageable pageable);

    List<Story> findTop10ByApprovedTrueOrderByLoveCountDesc();
    List<Story> findTop10ByApprovedTrueOrderByCreatedAtDesc();
    List<Story> findTop10ByApprovedTrueOrderByViewCountDesc();
    List<Story> findByFeaturedTrueAndApprovedTrue();

    @Query("SELECT s FROM Story s WHERE s.approved = true AND (s.title LIKE %:keyword% OR s.author LIKE %:keyword%)")
    Page<Story> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT s FROM Story s WHERE s.approved = true AND s.genre = :genre ORDER BY s.loveCount DESC")
    List<Story> findRecommendedByGenre(@Param("genre") Story.Genre genre, Pageable pageable);

    @Query("SELECT s FROM Story s WHERE s.approved = true ORDER BY s.loveCount DESC")
    List<Story> findMostLoved(Pageable pageable);

    @Query("SELECT s FROM Story s WHERE s.approved = true ORDER BY s.viewCount DESC")
    List<Story> findMostViewed(Pageable pageable);

    // For admin - all stories including pending
    Page<Story> findByApprovedFalse(Pageable pageable);

    // For user - their own stories
    List<Story> findByCreatedByUserId(Long userId);
}
