package com.inkandreams.service;

import com.inkandreams.entity.WriterProfile;
import com.inkandreams.repository.WriterProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WriterProfileService {

    private final WriterProfileRepository writerProfileRepository;

    public WriterProfile getProfile() {
        return writerProfileRepository.findAll().stream().findFirst()
                .orElse(new WriterProfile());
    }

    public WriterProfile updateProfile(WriterProfile profile) {
        WriterProfile existing = writerProfileRepository.findAll().stream().findFirst()
                .orElse(new WriterProfile());
        existing.setPhotoUrl(profile.getPhotoUrl());
        existing.setBiography(profile.getBiography());
        existing.setWritingJourney(profile.getWritingJourney());
        existing.setAchievements(profile.getAchievements());
        existing.setNumberOfStoriesWritten(profile.getNumberOfStoriesWritten());
        existing.setSocialMediaLinks(profile.getSocialMediaLinks());
        return writerProfileRepository.save(existing);
    }
}
