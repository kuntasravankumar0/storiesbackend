package com.inkandreams.service;

import com.inkandreams.dto.CommentDTO;
import com.inkandreams.entity.Comment;
import com.inkandreams.entity.Story;
import com.inkandreams.entity.User;
import com.inkandreams.repository.CommentRepository;
import com.inkandreams.repository.StoryRepository;
import com.inkandreams.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final StoryRepository storyRepository;
    private final UserRepository userRepository;

    public List<CommentDTO> getApprovedComments(Long storyId) {
        return commentRepository.findByStoryIdAndApprovedTrue(storyId).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public CommentDTO addComment(Long storyId, String email, String content) {
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new RuntimeException("Story not found"));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = Comment.builder()
                .content(content)
                .user(user)
                .story(story)
                .approved(false) // Needs admin approval
                .build();

        return toDTO(commentRepository.save(comment));
    }

    public List<CommentDTO> getUserComments(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return commentRepository.findByUserId(user.getId()).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    // Admin methods
    public Page<CommentDTO> getPendingComments(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return commentRepository.findByApprovedFalse(pageable).map(this::toDTO);
    }

    public void approveComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setApproved(true);
        commentRepository.save(comment);
    }

    public void rejectComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    private CommentDTO toDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setUsername(comment.getUser().getUsername());
        dto.setUserProfilePicture(comment.getUser().getProfilePictureUrl());
        dto.setStoryId(comment.getStory().getId());
        dto.setStoryTitle(comment.getStory().getTitle());
        dto.setApproved(comment.isApproved());
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }
}
