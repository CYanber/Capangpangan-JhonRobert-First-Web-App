package com.userhub.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Server-side stored session token. Deleting the row invalidates the token (server-side logout).
 */
@Entity
@Table(name = "auth_tokens")
public class AuthToken {

    @Id
    @Column(length = 64)
    private String token;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    public AuthToken() {
    }

    public AuthToken(String token, User user, LocalDateTime expiresAt) {
        this.token = token;
        this.user = user;
        this.expiresAt = expiresAt;
    }

    public String getToken() { return token; }
    public User getUser() { return user; }
    public LocalDateTime getExpiresAt() { return expiresAt; }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
}
