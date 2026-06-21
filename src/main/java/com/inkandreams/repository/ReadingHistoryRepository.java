package com.inkandreams.repository;

import com.inkandreams.entity.ReadingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReadingHistoryRepository extends JpaRepository<ReadingHistory, Long> {
    List<ReadingHistory> findByUserIdOrderByReadAtDesc(Long userId);
    List<ReadingHistory> findByUserIdAndContentTypeOrderByReadAtDesc(Long userId, ReadingHistory.ContentType contentType);
}
