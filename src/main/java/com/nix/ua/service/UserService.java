package com.nix.ua.service;

import com.nix.ua.config.PasswordConfig;
import com.nix.ua.model.User;
import com.nix.ua.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService extends MainService<User> implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordConfig passwordConfig;

    @Autowired
    public UserService(UserRepository userRepository, PasswordConfig passwordConfig) {
        super(userRepository);
        this.userRepository = userRepository;
        this.passwordConfig = passwordConfig;
    }

    @Override
    public User create(User item) {
        return userRepository.save(User.builder()
                .name(item.getName())
                .surname(item.getSurname())
                .email(item.getEmail())
                .username(item.getUsername())
                .password(passwordConfig.getPasswordEncoder().encode(item.getPassword()))
                .phone(item.getPhone())
                .avatar(item.getAvatar())
                .role(item.getRole())
                .build());
    }

    @Override
    public User update(User item) {
        if (findById(item.getId()).isPresent()) {
            return userRepository.save(User.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .surname(item.getSurname())
                    .email(item.getEmail())
                    .username(item.getUsername())
                    .password(item.getPassword())
                    .phone(item.getPhone())
                    .avatar(item.getAvatar())
                    .role(item.getRole())
                    .build());
        } else {
            return null;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}