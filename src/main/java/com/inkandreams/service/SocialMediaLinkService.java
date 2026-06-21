package com.inkandreams.service;

import com.inkandreams.entity.SocialMediaLink;
import com.inkandreams.repository.SocialMediaLinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SocialMediaLinkService {

    private final SocialMediaLinkRepository socialMediaLinkRepository;

    public List<SocialMediaLink> getAllLinks() {
        return socialMediaLinkRepository.findAll();
    }

    public SocialMediaLink createLink(SocialMediaLink link) {
        return socialMediaLinkRepository.save(link);
    }

    public SocialMediaLink updateLink(Long id, SocialMediaLink link) {
        SocialMediaLink existing = socialMediaLinkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Link not found"));
        existing.setPlatform(link.getPlatform());
        existing.setUrl(link.getUrl());
        existing.setIconUrl(link.getIconUrl());
        return socialMediaLinkRepository.save(existing);
    }

    public void deleteLink(Long id) {
        socialMediaLinkRepository.deleteById(id);
    }
}
