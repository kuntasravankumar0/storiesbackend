package com.inkandreams.service;

import com.inkandreams.dto.StoryCreateRequest;
import com.inkandreams.dto.StoryDTO;
import com.inkandreams.entity.*;
import com.inkandreams.repository.*;
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
public class StoryService {

    private final StoryRepository storyRepository;
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;
    private final ReadingProgressRepository readingProgressRepository;
    private final UserStoryLoveRepository userStoryLoveRepository;
    private final UserStoryBookmarkRepository userStoryBookmarkRepository;
    private final ReadingHistoryRepository readingHistoryRepository;
    private final CommentRepository commentRepository;

    public Page<StoryDTO> getAllStories(int page, int size, String sortBy) {
        Sort sort = Sort.by(Sort.Direction.DESC, sortBy != null ? sortBy : "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);
        return storyRepository.findByApprovedTrue(pageable).map(this::toDTO);
    }

    public StoryDTO getStoryById(Long id) {
        Story story = storyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Story not found"));
        story.setViewCount(story.getViewCount() + 1);
        storyRepository.save(story);
        return toDTO(story);
    }

    public Page<StoryDTO> getStoriesByGenre(String genre, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return storyRepository.findByGenreAndApprovedTrue(Story.Genre.valueOf(genre.toUpperCase()), pageable).map(this::toDTO);
    }

    public Page<StoryDTO> getStoriesByLanguage(String language, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return storyRepository.findByLanguageAndApprovedTrue(Story.Language.valueOf(language.toUpperCase()), pageable).map(this::toDTO);
    }

    public Page<StoryDTO> getStoriesByReadingTime(int maxMinutes, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return storyRepository.findByReadingTimeMinutesLessThanEqualAndApprovedTrue(maxMinutes, pageable).map(this::toDTO);
    }

    public List<StoryDTO> getTrendingStories() {
        return storyRepository.findTop10ByApprovedTrueOrderByViewCountDesc().stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public List<StoryDTO> getLatestStories() {
        return storyRepository.findTop10ByApprovedTrueOrderByCreatedAtDesc().stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public List<StoryDTO> getMostLovedStories() {
        return storyRepository.findTop10ByApprovedTrueOrderByLoveCountDesc().stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public List<StoryDTO> getFeaturedStories() {
        return storyRepository.findByFeaturedTrueAndApprovedTrue().stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public Page<StoryDTO> searchStories(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return storyRepository.searchByKeyword(keyword, pageable).map(this::toDTO);
    }

    public List<StoryDTO> getRecommendations(String genre) {
        Pageable pageable = PageRequest.of(0, 10);
        return storyRepository.findRecommendedByGenre(Story.Genre.valueOf(genre.toUpperCase()), pageable)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public java.util.Map<String, Boolean> getUserStoryStatus(Long storyId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        boolean loved = userStoryLoveRepository.existsByUserIdAndStoryId(user.getId(), storyId);
        boolean bookmarked = userStoryBookmarkRepository.existsByUserIdAndStoryId(user.getId(), storyId);
        return java.util.Map.of("loved", loved, "bookmarked", bookmarked);
    }

    public List<StoryDTO> getLovedStories(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Long> storyIds = userStoryLoveRepository.findByUserId(user.getId())
                .stream().map(l -> l.getStoryId()).collect(Collectors.toList());
        if (storyIds.isEmpty()) return List.of();
        return storyRepository.findAllById(storyIds).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<StoryDTO> getBookmarkedStories(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Long> storyIds = userStoryBookmarkRepository.findByUserId(user.getId())
                .stream().map(b -> b.getStoryId()).collect(Collectors.toList());
        if (storyIds.isEmpty()) return List.of();
        return storyRepository.findAllById(storyIds).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public void loveStory(Long storyId, String email) {
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new RuntimeException("Story not found"));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userStoryLoveRepository.existsByUserIdAndStoryId(user.getId(), storyId)) {
            userStoryLoveRepository.deleteByUserIdAndStoryId(user.getId(), storyId);
            story.setLoveCount(Math.max(0, story.getLoveCount() - 1));
        } else {
            userStoryLoveRepository.save(UserStoryLove.builder().userId(user.getId()).storyId(storyId).build());
            story.setLoveCount(story.getLoveCount() + 1);
        }
        storyRepository.save(story);
    }

    @Transactional
    public void bookmarkStory(Long storyId, String email) {
        storyRepository.findById(storyId)
                .orElseThrow(() -> new RuntimeException("Story not found"));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userStoryBookmarkRepository.existsByUserIdAndStoryId(user.getId(), storyId)) {
            userStoryBookmarkRepository.deleteByUserIdAndStoryId(user.getId(), storyId);
        } else {
            userStoryBookmarkRepository.save(UserStoryBookmark.builder().userId(user.getId()).storyId(storyId).build());
        }
    }

    @Transactional
    public void rateStory(Long storyId, String email, int value) {
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new RuntimeException("Story not found"));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Rating rating = ratingRepository.findByUserIdAndStoryId(user.getId(), storyId)
                .orElse(Rating.builder().user(user).story(story).build());
        rating.setValue(value);
        ratingRepository.save(rating);

        Double avg = ratingRepository.findAverageRatingByStoryId(storyId);
        long count = ratingRepository.countByStoryId(storyId);
        story.setAverageRating(avg != null ? avg : 0);
        story.setRatingCount((int) count);
        storyRepository.save(story);
    }

    @Transactional
    public void updateReadingProgress(Long storyId, String email, int percentage) {
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new RuntimeException("Story not found"));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ReadingProgress progress = readingProgressRepository
                .findByUserIdAndStoryId(user.getId(), storyId)
                .orElse(ReadingProgress.builder().user(user).story(story).build());
        progress.setProgressPercentage(percentage);
        readingProgressRepository.save(progress);

        // Add to reading history
        ReadingHistory history = ReadingHistory.builder()
                .user(user).story(story).contentType(ReadingHistory.ContentType.STORY).build();
        readingHistoryRepository.save(history);
    }

    // Admin methods
    public StoryDTO createStory(StoryCreateRequest request) {
        String imageUrls = request.getInlineImageUrls() != null ? 
                String.join(",", request.getInlineImageUrls()) : null;
        Story story = Story.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .coverImageUrl(request.getCoverImageUrl())
                .author(request.getAuthor())
                .genre(Story.Genre.valueOf(request.getGenre().toUpperCase()))
                .language(Story.Language.valueOf(request.getLanguage().toUpperCase()))
                .readingTimeMinutes(request.getReadingTimeMinutes())
                .featured(request.isFeatured())
                .authorNote(request.getAuthorNote())
                .inlineImageUrls(imageUrls)
                .approved(true) // Admin stories are auto-approved
                .build();
        return toDTO(storyRepository.save(story));
    }

    public StoryDTO updateStory(Long id, StoryCreateRequest request) {
        Story story = storyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Story not found"));
        story.setTitle(request.getTitle());
        story.setContent(request.getContent());
        story.setCoverImageUrl(request.getCoverImageUrl());
        story.setAuthor(request.getAuthor());
        story.setGenre(Story.Genre.valueOf(request.getGenre().toUpperCase()));
        story.setLanguage(Story.Language.valueOf(request.getLanguage().toUpperCase()));
        story.setReadingTimeMinutes(request.getReadingTimeMinutes());
        story.setFeatured(request.isFeatured());
        story.setAuthorNote(request.getAuthorNote());
        String imageUrls = request.getInlineImageUrls() != null ? 
                String.join(",", request.getInlineImageUrls()) : null;
        story.setInlineImageUrls(imageUrls);
        return toDTO(storyRepository.save(story));
    }

    public void deleteStory(Long id) {
        storyRepository.deleteById(id);
    }

    // Admin approval
    public Page<StoryDTO> getPendingStories(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return storyRepository.findByApprovedFalse(pageable).map(this::toDTO);
    }

    public void approveStory(Long id) {
        Story story = storyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Story not found"));
        story.setApproved(true);
        storyRepository.save(story);
    }

    public void rejectStory(Long id) {
        storyRepository.deleteById(id);
    }

    // User story creation
    public StoryDTO createUserStory(StoryCreateRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        String imageUrls = request.getInlineImageUrls() != null ? 
                String.join(",", request.getInlineImageUrls()) : null;
        String author = (request.getAuthor() != null && !request.getAuthor().isEmpty()) 
                ? request.getAuthor() : user.getUsername();
        Story story = Story.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .coverImageUrl(request.getCoverImageUrl())
                .author(author)
                .genre(Story.Genre.valueOf(request.getGenre().toUpperCase()))
                .language(Story.Language.valueOf(request.getLanguage().toUpperCase()))
                .readingTimeMinutes(request.getReadingTimeMinutes())
                .featured(false)
                .authorNote(request.getAuthorNote())
                .inlineImageUrls(imageUrls)
                .approved(false) // Needs admin approval
                .createdByUserId(user.getId())
                .build();
        return toDTO(storyRepository.save(story));
    }

    public List<StoryDTO> getUserStories(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return storyRepository.findByCreatedByUserId(user.getId()).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    private StoryDTO toDTO(Story story) {
        StoryDTO dto = new StoryDTO();
        dto.setId(story.getId());
        dto.setTitle(story.getTitle());
        dto.setContent(story.getContent());
        dto.setCoverImageUrl(story.getCoverImageUrl());
        dto.setAuthor(story.getAuthor());
        dto.setGenre(story.getGenre() != null ? story.getGenre().name() : null);
        dto.setLanguage(story.getLanguage() != null ? story.getLanguage().name() : null);
        dto.setReadingTimeMinutes(story.getReadingTimeMinutes());
        dto.setLoveCount(story.getLoveCount());
        dto.setViewCount(story.getViewCount());
        dto.setAverageRating(story.getAverageRating());
        dto.setRatingCount(story.getRatingCount());
        dto.setFeatured(story.isFeatured());
        dto.setAuthorNote(story.getAuthorNote());
        dto.setInlineImageUrlsFromString(story.getInlineImageUrls());
        dto.setCreatedAt(story.getCreatedAt());
        dto.setCommentCount((int) commentRepository.countByStoryId(story.getId()));
        return dto;
    }
}
