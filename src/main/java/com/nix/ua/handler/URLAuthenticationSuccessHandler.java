package com.nix.ua.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class URLAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private static URLAuthenticationSuccessHandler instance;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private URLAuthenticationSuccessHandler() {
    }

    public static URLAuthenticationSuccessHandler getInstance() {
        if (instance == null) {
            instance = new URLAuthenticationSuccessHandler();
        }
        return instance;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        handle(request, response, authentication);
        clearAuthenticationAttributes(request);
    }

    private void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        final String targetUrl = determineTargetUrl(authentication, request);

        if (!response.isCommitted()) {
            redirectStrategy.sendRedirect(request, response, targetUrl);
        } else {
            System.out.println("Response has already been committed. Unable to redirect to " + targetUrl);
        }
    }

    private String determineTargetUrl(final Authentication authentication, final HttpServletRequest request) {
        if (authentication.isAuthenticated()) {
            return request.getHeader("referer");
        } else {
            return "redirect:/api/home";
        }
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        final HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}