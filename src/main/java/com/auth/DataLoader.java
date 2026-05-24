package com.auth;

import com.auth.model.Role;
import com.auth.model.User;
import com.auth.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger LOGGER = LogManager.getLogger(DataLoader.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "admin@example.com";
        if (!userRepository.existsByEmail(adminEmail)) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("Admin123!"));
            admin.setRoles(Arrays.asList(Role.ADMIN, Role.USER));
            userRepository.save(admin);
            LOGGER.info("Admin user created: {} / password: Admin123!", adminEmail);
        } else {
            LOGGER.debug("Admin user already exists: {}", adminEmail);
        }
    }
}
