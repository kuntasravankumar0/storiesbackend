package com.inkandreams.controller;

import com.inkandreams.dto.UserDTO;
import com.inkandreams.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getProfile(Authentication auth) {
        return ResponseEntity.ok(userService.getUserProfile(auth.getName()));
    }

    @PutMapping("/profile")
    public ResponseEntity<UserDTO> updateProfile(@RequestBody Map<String, String> body,
                                                  Authentication auth) {
        return ResponseEntity.ok(userService.updateProfile(auth.getName(), body.get("profilePictureUrl")));
    }

    @PostMapping("/block-self")
    public ResponseEntity<Void> blockSelf(Authentication auth) {
        userService.blockUserByEmail(auth.getName());
        return ResponseEntity.ok().build();
    }
}
