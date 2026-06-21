package com.inkandreams.controller;

import com.inkandreams.dto.StoryCreateRequest;
import com.inkandreams.dto.StoryDTO;
import com.inkandreams.service.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stories")
@RequiredArgsConstructor
public class StoryController {

    private final StoryService storyService;

    @GetMapping
    public ResponseEntity<Page<StoryDTO>> getAllStories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String sortBy) {
        return ResponseEntity.ok(storyService.getAllStories(page, size, sortBy));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoryDTO> getStoryById(@PathVariable Long id) {
        return ResponseEntity.ok(storyService.getStoryById(id));
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<Page<StoryDTO>> getByGenre(
            @PathVariable String genre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        return ResponseEntity.ok(storyService.getStoriesByGenre(genre, page, size));
    }

    @GetMapping("/language/{language}")
    public ResponseEntity<Page<StoryDTO>> getByLanguage(
            @PathVariable String language,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        return ResponseEntity.ok(storyService.getStoriesByLanguage(language, page, size));
    }

    @GetMapping("/reading-time/{maxMinutes}")
    public ResponseEntity<Page<StoryDTO>> getByReadingTime(
            @PathVariable int maxMinutes,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        return ResponseEntity.ok(storyService.getStoriesByReadingTime(maxMinutes, page, size));
    }

    @GetMapping("/trending")
    public ResponseEntity<List<StoryDTO>> getTrending() {
        return ResponseEntity.ok(storyService.getTrendingStories());
    }

    @GetMapping("/latest")
    public ResponseEntity<List<StoryDTO>> getLatest() {
        return ResponseEntity.ok(storyService.getLatestStories());
    }

    @GetMapping("/most-loved")
    public ResponseEntity<List<StoryDTO>> getMostLoved() {
        return ResponseEntity.ok(storyService.getMostLovedStories());
    }

    @GetMapping("/featured")
    public ResponseEntity<List<StoryDTO>> getFeatured() {
        return ResponseEntity.ok(storyService.getFeaturedStories());
    }

    @GetMapping("/search")
    public ResponseEntity<Page<StoryDTO>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        return ResponseEntity.ok(storyService.searchStories(keyword, page, size));
    }

    @GetMapping("/recommendations/{genre}")
    public ResponseEntity<List<StoryDTO>> getRecommendations(@PathVariable String genre) {
        return ResponseEntity.ok(storyService.getRecommendations(genre));
    }

    @PostMapping("/{id}/love")
    public ResponseEntity<Void> loveStory(@PathVariable Long id, Authentication auth) {
        storyService.loveStory(id, auth.getName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/bookmark")
    public ResponseEntity<Void> bookmarkStory(@PathVariable Long id, Authentication auth) {
        storyService.bookmarkStory(id, auth.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<Map<String, Boolean>> getStoryStatus(@PathVariable Long id, Authentication auth) {
        return ResponseEntity.ok(storyService.getUserStoryStatus(id, auth.getName()));
    }

    @PostMapping("/{id}/rate")
    public ResponseEntity<Void> rateStory(@PathVariable Long id,
                                           @RequestBody Map<String, Integer> body,
                                           Authentication auth) {
        storyService.rateStory(id, auth.getName(), body.get("value"));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/progress")
    public ResponseEntity<Void> updateProgress(@PathVariable Long id,
                                                @RequestBody Map<String, Integer> body,
                                                Authentication auth) {
        storyService.updateReadingProgress(id, auth.getName(), body.get("percentage"));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/my-loved")
    public ResponseEntity<List<StoryDTO>> getMyLovedStories(Authentication auth) {
        return ResponseEntity.ok(storyService.getLovedStories(auth.getName()));
    }

    @GetMapping("/my-bookmarks")
    public ResponseEntity<List<StoryDTO>> getMyBookmarkedStories(Authentication auth) {
        return ResponseEntity.ok(storyService.getBookmarkedStories(auth.getName()));
    }

    @PostMapping("/user-create")
    public ResponseEntity<StoryDTO> createUserStory(@RequestBody StoryCreateRequest request, Authentication auth) {
        return ResponseEntity.ok(storyService.createUserStory(request, auth.getName()));
    }

    @GetMapping("/my-stories")
    public ResponseEntity<List<StoryDTO>> getMyStories(Authentication auth) {
        return ResponseEntity.ok(storyService.getUserStories(auth.getName()));
    }
}
