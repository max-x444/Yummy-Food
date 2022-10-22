package com.nix.ua.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/api/home")
public class MainController {
    @GetMapping
    public ModelAndView getHomePage(ModelAndView modelAndView) {
        modelAndView.setViewName("home");
        return modelAndView;
    }
}