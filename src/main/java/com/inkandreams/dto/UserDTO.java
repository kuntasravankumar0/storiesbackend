package com.inkandreams.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String mobileNumber;
    private String profilePictureUrl;
    private String role;
    private boolean blocked;
    private LocalDateTime createdAt;
}
