package com.nix.ua.controller;

import com.nix.ua.model.User;
import com.nix.ua.model.enums.Role;
import com.nix.ua.service.UserService;
import com.nix.ua.util.ImageUtil;
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
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ModelAndView getProfile(@AuthenticationPrincipal User user, @ModelAttribute @Valid User profile, ModelAndView modelAndView) {
        if (user == null) {
            modelAndView.setViewName("redirect:/api/home");
            return modelAndView;
        }
        if (profile.getId() == null) {
            profile = userService.findById(user.getId()).orElse(null);
        }
        modelAndView.addObject("profile", profile);
        modelAndView.setViewName("profile/profile");
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView create(User item, ModelAndView modelAndView) {
        item.setRole(Role.USER);
        userService.create(item);
        modelAndView.setViewName("redirect:/api/home");
        return modelAndView;
    }

    @PutMapping("/update")
    public ModelAndView update(@ModelAttribute("profile") @Valid User profile, BindingResult bindingResult,
                               @RequestParam("file") MultipartFile file, ModelAndView modelAndView) {
        System.out.println("2222222");
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("profile/profile");
            return modelAndView;
        }
        System.out.println("2222222");
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