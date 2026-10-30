package com.userhub.dto;

import com.userhub.entity.User;

import java.time.LocalDateTime;

/** Safe view of a user: never exposes the password hash. */
public class UserResponse {

    private final Long id;
    private final String username;
    private final String email;
    private final String fullName;
    private final String role;
    private final LocalDateTime createdAt;

    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.fullName = user.getFullName();
        this.role = user.getRole().name();
        this.createdAt = user.getCreatedAt();
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getFullName() { return fullName; }
    public String getRole() { return role; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
