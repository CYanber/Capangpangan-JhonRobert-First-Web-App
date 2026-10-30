package com.userhub.security;

import com.userhub.entity.Role;
import com.userhub.entity.User;
import com.userhub.exception.ApiException;
import com.userhub.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

/**
 * Token authentication + role-based authorization.
 * Reads "Authorization: Bearer <token>", validates it against the database and
 * exposes the user/token as request attributes.
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    public static final String ATTR_USER = "currentUser";
    public static final String ATTR_TOKEN = "currentToken";

    private final AuthService authService;

    public AuthInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod method)) {
            return true; // static resources, CORS preflight, etc.
        }

        RequiresAuth rule = method.getMethodAnnotation(RequiresAuth.class);
        if (rule == null) {
            rule = method.getBeanType().getAnnotation(RequiresAuth.class);
        }
        if (rule == null) {
            return true; // public endpoint
        }

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Missing or invalid Authorization header");
        }
        String token = header.substring(7).trim();

        User user = authService.authenticate(token)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Invalid or expired token"));

        Role[] required = rule.role();
        if (required.length > 0 && !Arrays.asList(required).contains(user.getRole())) {
            throw new ApiException(HttpStatus.FORBIDDEN, "You do not have permission to access this resource");
        }

        request.setAttribute(ATTR_USER, user);
        request.setAttribute(ATTR_TOKEN, token);
        return true;
    }
}
