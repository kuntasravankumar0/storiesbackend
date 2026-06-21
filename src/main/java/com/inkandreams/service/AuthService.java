package com.inkandreams.service;

import com.inkandreams.dto.AuthRequest;
import com.inkandreams.dto.AuthResponse;
import com.inkandreams.dto.RegisterRequest;
import com.inkandreams.entity.User;
import com.inkandreams.repository.UserRepository;
import com.inkandreams.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .mobileNumber(request.getMobileNumber())
                .role(User.Role.USER)
                .blocked(false)
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(token, user.getUsername(), user.getEmail(),
                user.getRole().name(), user.getId(), user.getProfilePictureUrl());
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        if (user.isBlocked()) {
            throw new RuntimeException("Your account has been blocked. Please contact admin.");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(token, user.getUsername(), user.getEmail(),
                user.getRole().name(), user.getId(), user.getProfilePictureUrl());
    }
}
