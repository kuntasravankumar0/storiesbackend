package com.inkandreams.dto;

import lombok.Data;
import java.util.List;

@Data
public class DashboardStats {
    private long totalUsers;
    private long totalStories;
    private long totalAudioStories;
    private long totalQuotes;
    private long pendingComments;
    private List<StoryDTO> topViewedStories;
    private List<StoryDTO> topLovedStories;
}
