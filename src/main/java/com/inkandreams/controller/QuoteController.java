package com.inkandreams.controller;

import com.inkandreams.dto.QuoteDTO;
import com.inkandreams.service.QuoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quotes")
@RequiredArgsConstructor
public class QuoteController {

    private final QuoteService quoteService;

    @GetMapping
    public ResponseEntity<Page<QuoteDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        return ResponseEntity.ok(quoteService.getAllQuotes(page, size));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Page<QuoteDTO>> getByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        return ResponseEntity.ok(quoteService.getByCategory(category, page, size));
    }

    @GetMapping("/popular")
    public ResponseEntity<List<QuoteDTO>> getPopular() {
        return ResponseEntity.ok(quoteService.getPopularQuotes());
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likeQuote(@PathVariable Long id, Authentication auth) {
        quoteService.likeQuote(id, auth.getName());
        return ResponseEntity.ok().build();
    }
}
