package com.userhub.service;

import com.userhub.dto.AuthResponse;
import com.userhub.dto.LoginRequest;
import com.userhub.dto.RegisterRequest;
import com.userhub.dto.UserResponse;
import com.userhub.entity.AuthToken;
import com.userhub.entity.Role;
import com.userhub.entity.User;
import com.userhub.exception.ApiException;
import com.userhub.repository.AuthTokenRepository;
import com.userhub.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private static final long TOKEN_HOURS = 8;

    private final UserRepository users;
    private final AuthTokenRepository tokens;
    private final PasswordEncoder encoder;

    public AuthService(UserRepository users, AuthTokenRepository tokens, PasswordEncoder encoder) {
        this.users = users;
        this.tokens = tokens;
        this.encoder = encoder;
    }

    @Transactional
    public UserResponse register(RegisterRequest req) {
        String username = req.getUsername().trim();
        String email = req.getEmail().trim().toLowerCase();

        if (users.existsByUsername(username)) {
            throw new ApiException(HttpStatus.CONFLICT, "Username is already taken");
        }
        if (users.existsByEmail(email)) {
            throw new ApiException(HttpStatus.CONFLICT, "Email is already registered");
        }
        // Public registration always creates a USER; ADMIN can only come from the seeder.
        User user = new User(username, email, req.getFullName().trim(),
                encoder.encode(req.getPassword()), Role.USER);
        return new UserResponse(users.save(user));
    }

    @Transactional
    public AuthResponse login(LoginRequest req) {
        User user = users.findByUsername(req.getUsername().trim())
                .filter(u -> encoder.matches(req.getPassword(), u.getPassword()))
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Invalid username or password"));

        String token = UUID.randomUUID().toString().replace("-", "")
                + UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        tokens.save(new AuthToken(token, user, LocalDateTime.now().plusHours(TOKEN_HOURS)));
        return new AuthResponse(token, new UserResponse(user));
    }

    /** Returns the user for a valid, unexpired token; empty otherwise. */
    @Transactional
    public Optional<User> authenticate(String token) {
        return tokens.findById(token).flatMap(t -> {
            if (t.isExpired()) {
                tokens.delete(t);
                return Optional.empty();
            }
            return Optional.of(t.getUser());
        });
    }

    /** Server-side logout: removes the token so it can never be used again. */
    @Transactional
    public void logout(String token) {
        tokens.deleteById(token);
    }
}
