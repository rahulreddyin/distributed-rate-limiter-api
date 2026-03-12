package com.rahul.ratelimiter.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahul.ratelimiter.dto.ErrorResponse;
import com.rahul.ratelimiter.service.RateLimiterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private final RateLimiterService rateLimiterService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RateLimitInterceptor(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getHeader("X-User-Id");

        if (userId == null || userId.isBlank()) {
            writeErrorResponse(
                    response,
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Bad Request",
                    "Missing X-User-Id header",
                    request.getRequestURI()
            );
            return false;
        }

        boolean allowed = rateLimiterService.isAllowed(userId, request.getRequestURI());

        if (!allowed) {
            writeErrorResponse(
                    response,
                    429,
                    "Too Many Requests",
                    "Rate limit exceeded. Try again later.",
                    request.getRequestURI()
            );
            return false;
        }

        return true;
    }

    private void writeErrorResponse(HttpServletResponse response, int status, String error, String message, String path) throws Exception {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now().toString(),
                status,
                error,
                message,
                path
        );

        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}