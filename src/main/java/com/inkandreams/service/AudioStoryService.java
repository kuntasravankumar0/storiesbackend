package com.inkandreams.service;

import com.inkandreams.dto.AudioStoryCreateRequest;
import com.inkandreams.dto.AudioStoryDTO;
import com.inkandreams.entity.AudioStory;
import com.inkandreams.entity.ReadingHistory;
import com.inkandreams.entity.Story;
import com.inkandreams.entity.User;
import com.inkandreams.repository.AudioStoryRepository;
import com.inkandreams.repository.ReadingHistoryRepository;
import com.inkandreams.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AudioStoryService {

    private final AudioStoryRepository audioStoryRepository;
    private final UserRepository userRepository;
    private final ReadingHistoryRepository readingHistoryRepository;

    public Page<AudioStoryDTO> getAllAudioStories(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return audioStoryRepository.findByApprovedTrue(pageable).map(this::toDTO);
    }

    public AudioStoryDTO getAudioStoryById(Long id) {
        AudioStory audioStory = audioStoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Audio story not found"));
        audioStory.setPlayCount(audioStory.getPlayCount() + 1);
        audioStoryRepository.save(audioStory);
        return toDTO(audioStory);
    }

    public Page<AudioStoryDTO> getByGenre(String genre, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return audioStoryRepository.findByGenreAndApprovedTrue(Story.Genre.valueOf(genre.toUpperCase()), pageable).map(this::toDTO);
    }

    public Page<AudioStoryDTO> getByLanguage(String language, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return audioStoryRepository.findByLanguageAndApprovedTrue(Story.Language.valueOf(language.toUpperCase()), pageable).map(this::toDTO);
    }

    public List<AudioStoryDTO> getMostPlayed() {
        return audioStoryRepository.findTop10ByApprovedTrueOrderByPlayCountDesc().stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public Page<AudioStoryDTO> search(String title, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return audioStoryRepository.findByTitleContainingIgnoreCaseAndApprovedTrue(title, pageable).map(this::toDTO);
    }

    public void recordListen(Long audioStoryId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        AudioStory audioStory = audioStoryRepository.findById(audioStoryId)
                .orElseThrow(() -> new RuntimeException("Audio story not found"));

        ReadingHistory history = ReadingHistory.builder()
                .user(user).audioStory(audioStory).contentType(ReadingHistory.ContentType.AUDIO).build();
        readingHistoryRepository.save(history);
    }

    // Admin methods
    public AudioStoryDTO createAudioStory(AudioStoryCreateRequest request) {
        AudioStory audioStory = AudioStory.builder()
                .title(request.getTitle())
                .coverImageUrl(request.getCoverImageUrl())
                .audioUrl(request.getAudioUrl())
                .author(request.getAuthor())
                .genre(Story.Genre.valueOf(request.getGenre().toUpperCase()))
                .language(Story.Language.valueOf(request.getLanguage().toUpperCase()))
                .durationSeconds(request.getDurationSeconds())
                .downloadable(request.isDownloadable())
                .build();
        return toDTO(audioStoryRepository.save(audioStory));
    }

    public AudioStoryDTO updateAudioStory(Long id, AudioStoryCreateRequest request) {
        AudioStory audioStory = audioStoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Audio story not found"));
        audioStory.setTitle(request.getTitle());
        audioStory.setCoverImageUrl(request.getCoverImageUrl());
        audioStory.setAudioUrl(request.getAudioUrl());
        audioStory.setAuthor(request.getAuthor());
        audioStory.setGenre(Story.Genre.valueOf(request.getGenre().toUpperCase()));
        audioStory.setLanguage(Story.Language.valueOf(request.getLanguage().toUpperCase()));
        audioStory.setDurationSeconds(request.getDurationSeconds());
        audioStory.setDownloadable(request.isDownloadable());
        return toDTO(audioStoryRepository.save(audioStory));
    }

    public void deleteAudioStory(Long id) {
        audioStoryRepository.deleteById(id);
    }

    // Admin approval
    public Page<AudioStoryDTO> getPendingAudios(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return audioStoryRepository.findByApprovedFalse(pageable).map(this::toDTO);
    }

    public void approveAudio(Long id) {
        AudioStory a = audioStoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        a.setApproved(true);
        audioStoryRepository.save(a);
    }

    public void rejectAudio(Long id) {
        audioStoryRepository.deleteById(id);
    }

    // User creation
    public AudioStoryDTO createUserAudio(AudioStoryCreateRequest request, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        String author = (request.getAuthor() != null && !request.getAuthor().isEmpty()) 
                ? request.getAuthor() : user.getUsername();
        AudioStory audioStory = AudioStory.builder()
                .title(request.getTitle())
                .coverImageUrl(request.getCoverImageUrl())
                .audioUrl(request.getAudioUrl())
                .author(author)
                .genre(Story.Genre.valueOf(request.getGenre().toUpperCase()))
                .language(Story.Language.valueOf(request.getLanguage().toUpperCase()))
                .durationSeconds(request.getDurationSeconds())
                .downloadable(false)
                .approved(false)
                .createdByUserId(user.getId())
                .build();
        return toDTO(audioStoryRepository.save(audioStory));
    }

    public List<AudioStoryDTO> getUserAudios(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return audioStoryRepository.findByCreatedByUserId(user.getId()).stream().map(this::toDTO).collect(Collectors.toList());
    }

    private AudioStoryDTO toDTO(AudioStory audioStory) {
        AudioStoryDTO dto = new AudioStoryDTO();
        dto.setId(audioStory.getId());
        dto.setTitle(audioStory.getTitle());
        dto.setCoverImageUrl(audioStory.getCoverImageUrl());
        dto.setAudioUrl(audioStory.getAudioUrl());
        dto.setAuthor(audioStory.getAuthor());
        dto.setGenre(audioStory.getGenre() != null ? audioStory.getGenre().name() : null);
        dto.setLanguage(audioStory.getLanguage() != null ? audioStory.getLanguage().name() : null);
        dto.setDurationSeconds(audioStory.getDurationSeconds());
        dto.setPlayCount(audioStory.getPlayCount());
        dto.setDownloadable(audioStory.isDownloadable());
        dto.setCreatedAt(audioStory.getCreatedAt());
        return dto;
    }
}
