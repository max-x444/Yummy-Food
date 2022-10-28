package com.nix.ua.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/home")
public class MainController implements ErrorController {
    @GetMapping
    public ModelAndView getHomePage(ModelAndView modelAndView) {
        modelAndView.setViewName("home");
        return modelAndView;
    }

    @GetMapping("/error/404")
    public ModelAndView getError(ModelAndView modelAndView) {
        modelAndView.setViewName("error/error");
        return modelAndView;
    }

    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletResponse response, HttpServletRequest request, ModelAndView modelAndView) {
        final int status = response.getStatus();
        if (status == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            modelAndView.addObject("reasonPhrase", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        } else if (status == HttpStatus.NOT_FOUND.value()) {
            modelAndView.addObject("reasonPhrase", HttpStatus.NOT_FOUND.getReasonPhrase());
        }
        modelAndView.addObject("dispatcherType", request.getDispatcherType());
        modelAndView.addObject("status", response.getStatus());
        modelAndView.setViewName("error/error");
        return modelAndView;
    }
}