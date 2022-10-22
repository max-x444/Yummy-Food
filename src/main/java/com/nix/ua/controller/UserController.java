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
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("profile/profile");
            return modelAndView;
        }

        ImageUtil.saveImage(file);
        final Optional<User> userOptional = userService.findById(profile.getId());

        if (userOptional.isPresent()) {
            final User updateUser = userOptional.get();
            updateUser.setName(profile.getName());
            updateUser.setSurname(profile.getSurname());
            updateUser.setUsername(profile.getUsername());
            updateUser.setPhone(profile.getPhone());
            updateUser.setEmail(profile.getEmail());
            updateUser.setAvatar(profile.getAvatar());
            return checkUsernameAndEmail(updateUser, modelAndView);
        }
        modelAndView.setViewName("redirect:/api/home");
        return modelAndView;
    }

    private ModelAndView checkUsernameAndEmail(User updateUser, ModelAndView modelAndView) {
        if (checkUsername(updateUser.getId(), updateUser.getUsername())) {
            if (checkEmail(updateUser.getId(), updateUser.getEmail())) {
                System.out.println(updateUser.getAvatar());
                userService.update(updateUser);
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
        modelAndView.setViewName("redirect:/api/home");
        return modelAndView;
    }

    private boolean checkUsername(String id, String username) {
        final Optional<User> optionalUser = userService.findUserByUsername(username);
        return optionalUser.map(user -> user.getId().equals(id)).orElse(true);
    }

    private boolean checkEmail(String id, String email) {
        final Optional<User> optionalUser = userService.findUserByEmail(email);
        return optionalUser.map(user -> user.getId().equals(id)).orElse(true);
    }
}