package com.nix.ua.repository;

import com.nix.ua.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByEmail(String email);
}