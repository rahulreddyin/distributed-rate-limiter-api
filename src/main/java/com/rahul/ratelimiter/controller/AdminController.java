package com.rahul.ratelimiter.controller;

import com.rahul.ratelimiter.dto.RateLimitStatusResponse;
import com.rahul.ratelimiter.service.RateLimiterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    private final RateLimiterService rateLimiterService;

    public AdminController(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @GetMapping("/admin/ratelimit/status")
    public RateLimitStatusResponse getRateLimitStatus(
            @RequestParam String userId,
            @RequestParam String endpoint
    ) {
        long currentRequests = rateLimiterService.getCurrentRequests(userId, endpoint);
        int maxRequests = rateLimiterService.getMaxRequestsForEndpoint(endpoint);
        int remainingRequests = Math.max(0, maxRequests - (int) currentRequests);
        int windowSeconds = rateLimiterService.getWindowSeconds();

        return new RateLimitStatusResponse(
                userId,
                endpoint,
                currentRequests,
                maxRequests,
                remainingRequests,
                windowSeconds
        );
    }
}