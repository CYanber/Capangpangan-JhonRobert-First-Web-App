package com.userhub.controller;

import com.userhub.dto.AuthResponse;
import com.userhub.dto.LoginRequest;
import com.userhub.dto.MessageResponse;
import com.userhub.dto.RegisterRequest;
import com.userhub.dto.UserResponse;
import com.userhub.security.AuthInterceptor;
import com.userhub.security.RequiresAuth;
import com.userhub.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @RequiresAuth
    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout(HttpServletRequest request) {
        String token = (String) request.getAttribute(AuthInterceptor.ATTR_TOKEN);
        authService.logout(token);
        return ResponseEntity.ok(new MessageResponse("Logged out successfully"));
    }
}
