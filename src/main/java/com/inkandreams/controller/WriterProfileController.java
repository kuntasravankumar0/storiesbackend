package com.inkandreams.controller;

import com.inkandreams.entity.WriterProfile;
import com.inkandreams.service.WriterProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/writer-profile")
@RequiredArgsConstructor
public class WriterProfileController {

    private final WriterProfileService writerProfileService;

    @GetMapping
    public ResponseEntity<WriterProfile> getProfile() {
        return ResponseEntity.ok(writerProfileService.getProfile());
    }
}
