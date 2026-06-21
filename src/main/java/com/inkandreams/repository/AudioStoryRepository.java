package com.inkandreams.repository;

import com.inkandreams.entity.AudioStory;
import com.inkandreams.entity.Story;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AudioStoryRepository extends JpaRepository<AudioStory, Long> {
    Page<AudioStory> findByGenreAndApprovedTrue(Story.Genre genre, Pageable pageable);
    Page<AudioStory> findByLanguageAndApprovedTrue(Story.Language language, Pageable pageable);
    List<AudioStory> findTop10ByApprovedTrueOrderByPlayCountDesc();
    Page<AudioStory> findByTitleContainingIgnoreCaseAndApprovedTrue(String title, Pageable pageable);
    Page<AudioStory> findByApprovedTrue(Pageable pageable);
    Page<AudioStory> findByApprovedFalse(Pageable pageable);
    List<AudioStory> findByCreatedByUserId(Long userId);
}
