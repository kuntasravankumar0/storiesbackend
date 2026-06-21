package com.inkandreams.controller;

import com.inkandreams.entity.SocialMediaLink;
import com.inkandreams.service.SocialMediaLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/social-links")
@RequiredArgsConstructor
public class SocialMediaLinkController {

    private final SocialMediaLinkService socialMediaLinkService;

    @GetMapping
    public ResponseEntity<List<SocialMediaLink>> getAll() {
        return ResponseEntity.ok(socialMediaLinkService.getAllLinks());
    }
}
