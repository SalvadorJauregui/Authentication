package com.auth.repository;

import com.auth.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    Optional<User> findByEmail(String email);

    Optional<User> findById(UUID id);

    boolean existsByEmail(String email);

    User save(User user);
}
