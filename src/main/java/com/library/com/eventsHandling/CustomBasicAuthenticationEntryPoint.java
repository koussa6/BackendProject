package com.library.com.eventsHandling;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class CustomBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("error-reason","Invalid credentials");
        String json = """
        {
        "timestamps":"%s",
            "error": "Unauthorized",
            "message": "%s",
            "status": 401,
            "path":"%s"
            
        }
        """.formatted(
                java.time.LocalDateTime.now(),
                authException.getMessage(),
                request.getRequestURI()
        );

        response.getWriter().write(json);    }
}
