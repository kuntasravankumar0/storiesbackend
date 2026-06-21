package com.inkandreams.controller;

import com.inkandreams.dto.CommentDTO;
import com.inkandreams.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/story/{storyId}")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Long storyId) {
        return ResponseEntity.ok(commentService.getApprovedComments(storyId));
    }

    @PostMapping("/story/{storyId}")
    public ResponseEntity<CommentDTO> addComment(@PathVariable Long storyId,
                                                  @RequestBody Map<String, String> body,
                                                  Authentication auth) {
        return ResponseEntity.ok(commentService.addComment(storyId, auth.getName(), body.get("content")));
    }

    @GetMapping("/my-comments")
    public ResponseEntity<List<CommentDTO>> getMyComments(Authentication auth) {
        return ResponseEntity.ok(commentService.getUserComments(auth.getName()));
    }
}
