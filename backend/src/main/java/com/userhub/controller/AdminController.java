package com.userhub.controller;

import com.userhub.dto.UserResponse;
import com.userhub.entity.Role;
import com.userhub.security.RequiresAuth;
import com.userhub.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** ADMIN-only endpoints. */
@RestController
@RequestMapping("/api/admin")
@RequiresAuth(role = Role.ADMIN)
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> allUsers() {
        return ResponseEntity.ok(userService.listAll());
    }
}
