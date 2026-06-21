package com.inkandreams.repository;

import com.inkandreams.entity.Quote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long> {
    Page<Quote> findByCategory(Quote.Category category, Pageable pageable);
    List<Quote> findTop10ByOrderByLikeCountDesc();
}
