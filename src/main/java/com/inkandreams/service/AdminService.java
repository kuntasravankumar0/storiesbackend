package com.inkandreams.service;

import com.inkandreams.dto.DashboardStats;
import com.inkandreams.dto.StoryDTO;
import com.inkandreams.entity.Story;
import com.inkandreams.entity.User;
import com.inkandreams.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final StoryRepository storyRepository;
    private final AudioStoryRepository audioStoryRepository;
    private final QuoteRepository quoteRepository;
    private final CommentRepository commentRepository;

    public DashboardStats getDashboardStats() {
        DashboardStats stats = new DashboardStats();
        stats.setTotalUsers(userRepository.countByRole(User.Role.USER));
        stats.setTotalStories(storyRepository.count());
        stats.setTotalAudioStories(audioStoryRepository.count());
        stats.setTotalQuotes(quoteRepository.count());
        stats.setPendingComments(commentRepository.findByApprovedFalse(PageRequest.of(0, 1)).getTotalElements());

        // Top 5 Most Viewed
        List<Story> mostViewed = storyRepository.findMostViewed(PageRequest.of(0, 5));
        List<StoryDTO> topViewed = new ArrayList<>();
        for (Story s : mostViewed) {
            StoryDTO dto = new StoryDTO();
            dto.setId(s.getId());
            dto.setTitle(s.getTitle());
            dto.setViewCount(s.getViewCount());
            dto.setAuthor(s.getAuthor());
            topViewed.add(dto);
        }
        stats.setTopViewedStories(topViewed);

        // Top 5 Most Loved
        List<Story> mostLoved = storyRepository.findMostLoved(PageRequest.of(0, 5));
        List<StoryDTO> topLoved = new ArrayList<>();
        for (Story s : mostLoved) {
            StoryDTO dto = new StoryDTO();
            dto.setId(s.getId());
            dto.setTitle(s.getTitle());
            dto.setLoveCount(s.getLoveCount());
            dto.setAuthor(s.getAuthor());
            topLoved.add(dto);
        }
        stats.setTopLovedStories(topLoved);

        return stats;
    }
}
