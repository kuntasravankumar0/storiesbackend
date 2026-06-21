package com.inkandreams.controller;

import com.inkandreams.dto.AudioStoryCreateRequest;
import com.inkandreams.dto.AudioStoryDTO;
import com.inkandreams.service.AudioStoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audio-stories")
@RequiredArgsConstructor
public class AudioStoryController {

    private final AudioStoryService audioStoryService;

    @GetMapping
    public ResponseEntity<Page<AudioStoryDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        return ResponseEntity.ok(audioStoryService.getAllAudioStories(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AudioStoryDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(audioStoryService.getAudioStoryById(id));
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<Page<AudioStoryDTO>> getByGenre(
            @PathVariable String genre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        return ResponseEntity.ok(audioStoryService.getByGenre(genre, page, size));
    }

    @GetMapping("/language/{language}")
    public ResponseEntity<Page<AudioStoryDTO>> getByLanguage(
            @PathVariable String language,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        return ResponseEntity.ok(audioStoryService.getByLanguage(language, page, size));
    }

    @GetMapping("/most-played")
    public ResponseEntity<List<AudioStoryDTO>> getMostPlayed() {
        return ResponseEntity.ok(audioStoryService.getMostPlayed());
    }

    @GetMapping("/search")
    public ResponseEntity<Page<AudioStoryDTO>> search(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        return ResponseEntity.ok(audioStoryService.search(title, page, size));
    }

    @PostMapping("/{id}/listen")
    public ResponseEntity<Void> recordListen(@PathVariable Long id, Authentication auth) {
        audioStoryService.recordListen(id, auth.getName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user-create")
    public ResponseEntity<AudioStoryDTO> createUserAudio(@RequestBody AudioStoryCreateRequest request, Authentication auth) {
        return ResponseEntity.ok(audioStoryService.createUserAudio(request, auth.getName()));
    }

    @GetMapping("/my-audios")
    public ResponseEntity<List<AudioStoryDTO>> getMyAudios(Authentication auth) {
        return ResponseEntity.ok(audioStoryService.getUserAudios(auth.getName()));
    }
}
