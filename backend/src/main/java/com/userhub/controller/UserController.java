package com.userhub.controller;

import com.userhub.dto.MessageResponse;
import com.userhub.dto.UpdateProfileRequest;
import com.userhub.dto.UserResponse;
import com.userhub.entity.User;
import com.userhub.security.AuthInterceptor;
import com.userhub.security.RequiresAuth;
import com.userhub.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** Endpoints available to any authenticated user (USER or ADMIN). */
@RestController
@RequestMapping("/api/users")
@RequiresAuth
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /** Protected dashboard endpoint: current user's profile. */
    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(HttpServletRequest request) {
        User current = (User) request.getAttribute(AuthInterceptor.ATTR_USER);
        return ResponseEntity.ok(new UserResponse(current));
    }

    /** Protected dashboard endpoint: simple welcome payload. */
    @GetMapping("/dashboard")
    public ResponseEntity<MessageResponse> dashboard(HttpServletRequest request) {
        User current = (User) request.getAttribute(AuthInterceptor.ATTR_USER);
        return ResponseEntity.ok(new MessageResponse(
                "Welcome to your dashboard, " + current.getFullName() + "!"));
    }

    /** Edit My Profile: update own full name. */
    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMe(@Valid @RequestBody UpdateProfileRequest body,
                                                 HttpServletRequest request) {
        User current = (User) request.getAttribute(AuthInterceptor.ATTR_USER);
        return ResponseEntity.ok(userService.updateFullName(current.getId(), body));
    }
}
