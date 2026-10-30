package com.userhub.service;

import com.userhub.dto.UpdateProfileRequest;
import com.userhub.dto.UserResponse;
import com.userhub.entity.User;
import com.userhub.exception.ApiException;
import com.userhub.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository users;

    public UserService(UserRepository users) {
        this.users = users;
    }

    @Transactional(readOnly = true)
    public List<UserResponse> listAll() {
        return users.findAll().stream().map(UserResponse::new).toList();
    }

    @Transactional
    public UserResponse updateFullName(Long userId, UpdateProfileRequest req) {
        User user = users.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));
        user.setFullName(req.getFullName().trim());
        return new UserResponse(users.save(user));
    }
}
