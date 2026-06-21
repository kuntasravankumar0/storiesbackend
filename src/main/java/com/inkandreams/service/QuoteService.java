package com.inkandreams.service;

import com.inkandreams.dto.QuoteDTO;
import com.inkandreams.entity.Quote;
import com.inkandreams.entity.User;
import com.inkandreams.entity.UserQuoteLike;
import com.inkandreams.repository.QuoteRepository;
import com.inkandreams.repository.UserQuoteLikeRepository;
import com.inkandreams.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuoteService {

    private final QuoteRepository quoteRepository;
    private final UserRepository userRepository;
    private final UserQuoteLikeRepository userQuoteLikeRepository;

    public Page<QuoteDTO> getAllQuotes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return quoteRepository.findAll(pageable).map(this::toDTO);
    }

    public Page<QuoteDTO> getByCategory(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return quoteRepository.findByCategory(Quote.Category.valueOf(category.toUpperCase()), pageable).map(this::toDTO);
    }

    public List<QuoteDTO> getPopularQuotes() {
        return quoteRepository.findTop10ByOrderByLikeCountDesc().stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public void likeQuote(Long quoteId, String email) {
        Quote quote = quoteRepository.findById(quoteId)
                .orElseThrow(() -> new RuntimeException("Quote not found"));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userQuoteLikeRepository.existsByUserIdAndQuoteId(user.getId(), quoteId)) {
            userQuoteLikeRepository.deleteByUserIdAndQuoteId(user.getId(), quoteId);
            quote.setLikeCount(Math.max(0, quote.getLikeCount() - 1));
        } else {
            userQuoteLikeRepository.save(UserQuoteLike.builder().userId(user.getId()).quoteId(quoteId).build());
            quote.setLikeCount(quote.getLikeCount() + 1);
        }
        quoteRepository.save(quote);
    }

    // Admin methods
    public QuoteDTO createQuote(QuoteDTO request) {
        Quote quote = Quote.builder()
                .text(request.getText())
                .author(request.getAuthor())
                .category(Quote.Category.valueOf(request.getCategory().toUpperCase()))
                .build();
        return toDTO(quoteRepository.save(quote));
    }

    public QuoteDTO updateQuote(Long id, QuoteDTO request) {
        Quote quote = quoteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quote not found"));
        quote.setText(request.getText());
        quote.setAuthor(request.getAuthor());
        quote.setCategory(Quote.Category.valueOf(request.getCategory().toUpperCase()));
        return toDTO(quoteRepository.save(quote));
    }

    public void deleteQuote(Long id) {
        quoteRepository.deleteById(id);
    }

    private QuoteDTO toDTO(Quote quote) {
        QuoteDTO dto = new QuoteDTO();
        dto.setId(quote.getId());
        dto.setText(quote.getText());
        dto.setAuthor(quote.getAuthor());
        dto.setCategory(quote.getCategory() != null ? quote.getCategory().name() : null);
        dto.setLikeCount(quote.getLikeCount());
        dto.setCreatedAt(quote.getCreatedAt());
        return dto;
    }
}
