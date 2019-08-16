package ru.tsystems.sbb.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(CustomAccessDeniedHandler.class);

    @Override
    public void handle(final HttpServletRequest request,
                       final HttpServletResponse response,
                       final AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        Authentication auth
                = SecurityContextHolder.getContext().getAuthentication();
        String sanitizedURI = sanitize(request.getRequestURI());
        String sanitizedName = sanitize(auth.getName());
        LOGGER.warn("User {} attempted to access protected URL: {}",
                sanitizedName, sanitizedURI);
        response.sendRedirect(request.getContextPath() + "/accessDenied");
    }

    private String sanitize(final String s) {
        return s.replaceAll("[\\n|\\r|\\t]", "_");
    }
}
