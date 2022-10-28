package com.nix.ua.service;

import com.nix.ua.config.PasswordConfig;
import com.nix.ua.model.User;
import com.nix.ua.model.enums.Role;
import com.nix.ua.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;


class UserServiceTest {
    private static final String USERNAME = "user";
    private static final String EMAIL = "user@gmail.com";
    private UserService target;
    private UserRepository targetRepository;
    private ModelAndView modelAndView;

    @BeforeEach
    void setUp() {
        final PasswordConfig passwordConfig = Mockito.mock(PasswordConfig.class);
        targetRepository = Mockito.mock(UserRepository.class);
        modelAndView = Mockito.mock(ModelAndView.class);
        target = new UserService(targetRepository, passwordConfig);
    }

    @Test
    void loadUserByUsernameFounded() {
        Mockito.when(targetRepository.findUserByUsername(USERNAME)).thenReturn(Optional.of(createDefaultUser()));
        Assertions.assertEquals(createDefaultUser(), target.loadUserByUsername(USERNAME));
    }

    @Test
    void loadUserByUsernameNotFounded() {
        Mockito.when(targetRepository.findUserByUsername(USERNAME)).thenReturn(Optional.of(createDefaultUser()));
        Assertions.assertThrows(UsernameNotFoundException.class, () -> target.loadUserByUsername(anyString()));
    }

    @Test
    void findUserByUsernameFounded() {
        Mockito.when(targetRepository.findUserByUsername(USERNAME)).thenReturn(Optional.of(createDefaultUser()));
        Assertions.assertEquals(Optional.of(createDefaultUser()), target.findUserByUsername(USERNAME));
    }

    @Test
    void findUserByUsernameNotFounded() {
        Mockito.when(targetRepository.findUserByUsername(USERNAME)).thenReturn(Optional.of(createDefaultUser()));
        Assertions.assertNotEquals(Optional.of(createDefaultUser()), target.findUserByUsername(anyString()));
    }

    @Test
    void findUserByEmailFounded() {
        Mockito.when(targetRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(createDefaultUser()));
        Assertions.assertEquals(Optional.of(createDefaultUser()), target.findUserByEmail(EMAIL));
    }

    @Test
    void findUserByEmailNotFounded() {
        Mockito.when(targetRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(createDefaultUser()));
        Assertions.assertNotEquals(Optional.of(createDefaultUser()), target.findUserByEmail(anyString()));
    }

    @Test
    void checkUsernameAndEmailErrorUsername() {
        Mockito.when(targetRepository.findUserByUsername(USERNAME)).thenReturn(Optional.of(createDefaultUser()));
        Assertions.assertEquals(getUsernameError(), target.checkUsernameAndEmail(createDefaultUser(), modelAndView));
    }

    @Test
    void checkUsernameAndEmailErrorEmail() {
        Mockito.when(targetRepository.findUserByUsername(EMAIL)).thenReturn(Optional.of(createDefaultUser()));
        Assertions.assertEquals(getEmailError(), target.checkUsernameAndEmail(createDefaultUser(), modelAndView));
    }

    @Test
    void checkUsernameAndEmailValidData() {
        Mockito.when(targetRepository.findUserByUsername(anyString())).thenReturn(Optional.empty());
        Assertions.assertEquals(getValidData(), target.checkUsernameAndEmail(createDefaultUser(), modelAndView));
    }

    private User createDefaultUser() {
        return User.builder()
                .id("5ef15243-4de1-4345-b48e-879b5631eb6b")
                .avatar("https://i.ibb.co/0X4D18p/avatar-png.png")
                .email(EMAIL)
                .name("user")
                .surname("user")
                .username(USERNAME)
                .password("$2a$10$/335nAy/.StBB1pBuO4iaeuK6Sq6uuMgz28EeURCkvSnDL/olevNi")
                .role(Role.USER)
                .phone("+380677843596")
                .build();
    }

    private ModelAndView getEmailError() {
        modelAndView.addObject("errorEmail", "This email already exists");
        modelAndView.setViewName("profile/profile");
        return modelAndView;
    }

    private ModelAndView getUsernameError() {
        modelAndView.addObject("errorUsername", "This username already exists");
        modelAndView.setViewName("profile/profile");
        return modelAndView;
    }

    private ModelAndView getValidData() {
        modelAndView.setViewName("redirect:/api/user/profile");
        return modelAndView;
    }
}