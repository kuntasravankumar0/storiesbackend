package com.inkandreams.controller;

import com.inkandreams.dto.*;
import com.inkandreams.entity.SocialMediaLink;
import com.inkandreams.entity.WriterProfile;
import com.inkandreams.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final StoryService storyService;
    private final AudioStoryService audioStoryService;
    private final QuoteService quoteService;
    private final CommentService commentService;
    private final UserService userService;
    private final AdminService adminService;
    private final WriterProfileService writerProfileService;
    private final SocialMediaLinkService socialMediaLinkService;

    // Dashboard
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStats> getDashboard() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    // Story Management
    @PostMapping("/stories")
    public ResponseEntity<StoryDTO> createStory(@Valid @RequestBody StoryCreateRequest request) {
        return ResponseEntity.ok(storyService.createStory(request));
    }

    @PutMapping("/stories/{id}")
    public ResponseEntity<StoryDTO> updateStory(@PathVariable Long id,
                                                 @Valid @RequestBody StoryCreateRequest request) {
        return ResponseEntity.ok(storyService.updateStory(id, request));
    }

    @DeleteMapping("/stories/{id}")
    public ResponseEntity<Void> deleteStory(@PathVariable Long id) {
        storyService.deleteStory(id);
        return ResponseEntity.ok().build();
    }

    // Story Approval
    @GetMapping("/stories/pending")
    public ResponseEntity<Page<StoryDTO>> getPendingStories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(storyService.getPendingStories(page, size));
    }

    @PutMapping("/stories/{id}/approve")
    public ResponseEntity<Void> approveStory(@PathVariable Long id) {
        storyService.approveStory(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/stories/{id}/reject")
    public ResponseEntity<Void> rejectStory(@PathVariable Long id) {
        storyService.rejectStory(id);
        return ResponseEntity.ok().build();
    }

    // Audio Story Management
    @PostMapping("/audio-stories")
    public ResponseEntity<AudioStoryDTO> createAudioStory(@Valid @RequestBody AudioStoryCreateRequest request) {
        return ResponseEntity.ok(audioStoryService.createAudioStory(request));
    }

    @PutMapping("/audio-stories/{id}")
    public ResponseEntity<AudioStoryDTO> updateAudioStory(@PathVariable Long id,
                                                           @Valid @RequestBody AudioStoryCreateRequest request) {
        return ResponseEntity.ok(audioStoryService.updateAudioStory(id, request));
    }

    @DeleteMapping("/audio-stories/{id}")
    public ResponseEntity<Void> deleteAudioStory(@PathVariable Long id) {
        audioStoryService.deleteAudioStory(id);
        return ResponseEntity.ok().build();
    }

    // Audio Story Approval
    @GetMapping("/audio-stories/pending")
    public ResponseEntity<Page<AudioStoryDTO>> getPendingAudios(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(audioStoryService.getPendingAudios(page, size));
    }

    @PutMapping("/audio-stories/{id}/approve")
    public ResponseEntity<Void> approveAudio(@PathVariable Long id) {
        audioStoryService.approveAudio(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/audio-stories/{id}/reject")
    public ResponseEntity<Void> rejectAudio(@PathVariable Long id) {
        audioStoryService.rejectAudio(id);
        return ResponseEntity.ok().build();
    }

    // Quote Management
    @PostMapping("/quotes")
    public ResponseEntity<QuoteDTO> createQuote(@RequestBody QuoteDTO request) {
        return ResponseEntity.ok(quoteService.createQuote(request));
    }

    @PutMapping("/quotes/{id}")
    public ResponseEntity<QuoteDTO> updateQuote(@PathVariable Long id, @RequestBody QuoteDTO request) {
        return ResponseEntity.ok(quoteService.updateQuote(id, request));
    }

    @DeleteMapping("/quotes/{id}")
    public ResponseEntity<Void> deleteQuote(@PathVariable Long id) {
        quoteService.deleteQuote(id);
        return ResponseEntity.ok().build();
    }

    // Comment Moderation
    @GetMapping("/comments/pending")
    public ResponseEntity<Page<CommentDTO>> getPendingComments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(commentService.getPendingComments(page, size));
    }

    @PutMapping("/comments/{id}/approve")
    public ResponseEntity<Void> approveComment(@PathVariable Long id) {
        commentService.approveComment(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok().build();
    }

    // User Management
    @GetMapping("/users")
    public ResponseEntity<Page<UserDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(userService.getAllUsers(page, size));
    }

    @PutMapping("/users/{id}/block")
    public ResponseEntity<Void> blockUser(@PathVariable Long id) {
        userService.blockUser(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    // Writer Profile
    @PutMapping("/writer-profile")
    public ResponseEntity<WriterProfile> updateWriterProfile(@RequestBody WriterProfile profile) {
        return ResponseEntity.ok(writerProfileService.updateProfile(profile));
    }

    // Social Media Links
    @PostMapping("/social-links")
    public ResponseEntity<SocialMediaLink> createSocialLink(@RequestBody SocialMediaLink link) {
        return ResponseEntity.ok(socialMediaLinkService.createLink(link));
    }

    @PutMapping("/social-links/{id}")
    public ResponseEntity<SocialMediaLink> updateSocialLink(@PathVariable Long id,
                                                             @RequestBody SocialMediaLink link) {
        return ResponseEntity.ok(socialMediaLinkService.updateLink(id, link));
    }

    @DeleteMapping("/social-links/{id}")
    public ResponseEntity<Void> deleteSocialLink(@PathVariable Long id) {
        socialMediaLinkService.deleteLink(id);
        return ResponseEntity.ok().build();
    }
}
