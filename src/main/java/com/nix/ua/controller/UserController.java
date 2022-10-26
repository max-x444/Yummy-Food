package com.nix.ua.controller;

import com.nix.ua.model.User;
import com.nix.ua.model.enums.Role;
import com.nix.ua.service.UserService;
import com.nix.ua.util.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ModelAndView getProfile(@AuthenticationPrincipal User profile, ModelAndView modelAndView) {
        profile = userService.findById(profile.getId()).orElse(null);
        modelAndView.addObject("profile", profile);
        modelAndView.setViewName("profile/profile");
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView create(User item, ModelAndView modelAndView) {
        item.setRole(Role.USER);
        LOGGER.info("Create user: {}", userService.create(item));
        modelAndView.setViewName("redirect:/api/home");
        return modelAndView;
    }

    @PutMapping("/update")
    public ModelAndView update(@ModelAttribute("profile") @Valid User profile, BindingResult bindingResult,
                               @RequestParam("file") MultipartFile file, ModelAndView modelAndView) {
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("profile/profile");
            return modelAndView;
        }
        final Optional<User> userOptional = userService.findById(profile.getId());
        final Optional<String> linkImage = ImageUtil.saveImage(file);
        linkImage.ifPresent(profile::setAvatar);
        if (userOptional.isPresent()) {
            final User updateUser = userOptional.get();
            updateUser.setName(profile.getName());
            updateUser.setSurname(profile.getSurname());
            updateUser.setUsername(profile.getUsername());
            updateUser.setPhone(profile.getPhone());
            updateUser.setEmail(profile.getEmail());
            updateUser.setAvatar(profile.getAvatar());
            return userService.checkUsernameAndEmail(updateUser, modelAndView);
        }
        modelAndView.setViewName("redirect:/api/user/profile");
        return modelAndView;
    }
}