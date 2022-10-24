package com.nix.ua.service;

import com.nix.ua.config.PasswordConfig;
import com.nix.ua.model.User;
import com.nix.ua.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

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

    public ModelAndView checkUsernameAndEmail(User updateUser, ModelAndView modelAndView) {
        if (checkUsername(updateUser.getId(), updateUser.getUsername())) {
            if (checkEmail(updateUser.getId(), updateUser.getEmail())) {
                update(updateUser);
            } else {
                modelAndView.addObject("errorEmail", "This email already exists");
                modelAndView.setViewName("profile/profile");
                return modelAndView;
            }
        } else {
            modelAndView.addObject("errorUsername", "This username already exists");
            modelAndView.setViewName("profile/profile");
            return modelAndView;
        }
        modelAndView.setViewName("redirect:/api/user/profile");
        return modelAndView;
    }

    private boolean checkUsername(String id, String username) {
        final Optional<User> optionalUser = findUserByUsername(username);
        return optionalUser.map(user -> user.getId().equals(id)).orElse(true);
    }

    private boolean checkEmail(String id, String email) {
        final Optional<User> optionalUser = findUserByEmail(email);
        return optionalUser.map(user -> user.getId().equals(id)).orElse(true);
    }
}