package com.userhub.config;

import com.userhub.entity.Role;
import com.userhub.entity.User;
import com.userhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/** Seeds a default ADMIN account on startup (only if it does not exist yet). */
@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedAdmin(UserRepository users,
                                PasswordEncoder encoder,
                                @Value("${userhub.admin.username}") String username,
                                @Value("${userhub.admin.password}") String password,
                                @Value("${userhub.admin.fullName}") String fullName,
                                @Value("${userhub.admin.email}") String email) {
        return args -> {
            if (!users.existsByUsername(username)) {
                users.save(new User(username, email, fullName, encoder.encode(password), Role.ADMIN));
                System.out.println("[UserHub] Seeded default ADMIN user: " + username);
            }
        };
    }
}
