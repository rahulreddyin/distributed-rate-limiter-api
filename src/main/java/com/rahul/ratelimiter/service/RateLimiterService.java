package com.rahul.ratelimiter.service;

import com.rahul.ratelimiter.model.RateLimitProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

@Service
public class RateLimiterService {

    private final StringRedisTemplate redisTemplate;
    private final RateLimitProperties rateLimitProperties;

    public RateLimiterService(StringRedisTemplate redisTemplate, RateLimitProperties rateLimitProperties) {
        this.redisTemplate = redisTemplate;
        this.rateLimitProperties = rateLimitProperties;
    }

    public boolean isAllowed(String userId, String endpoint) {
        int maxRequests = getMaxRequestsForEndpoint(endpoint);
        int windowSeconds = rateLimitProperties.getWindowSeconds();

        String redisKey = buildRedisKey(userId, endpoint);

        Long currentCount = redisTemplate.opsForValue().increment(redisKey);

        if (currentCount != null && currentCount == 1) {
            redisTemplate.expire(redisKey, Duration.ofSeconds(windowSeconds));
        }

        return currentCount != null && currentCount <= maxRequests;
    }

    public long getCurrentRequests(String userId, String endpoint) {
        String redisKey = buildRedisKey(userId, endpoint);
        String value = redisTemplate.opsForValue().get(redisKey);

        if (value == null) {
            return 0;
        }

        return Long.parseLong(value);
    }

    public int getMaxRequestsForEndpoint(String endpoint) {
        String endpointKey = getEndpointKey(endpoint);
        Map<String, Integer> limits = rateLimitProperties.getLimits();
        Integer maxRequests = limits.get(endpointKey);

        if (maxRequests == null) {
            return 5;
        }

        return maxRequests;
    }

    public int getWindowSeconds() {
        return rateLimitProperties.getWindowSeconds();
    }

    private String buildRedisKey(String userId, String endpoint) {
        return "rate_limit:" + userId + ":" + endpoint;
    }

    private String getEndpointKey(String endpoint) {
        if ("/api/data".equals(endpoint)) {
            return "data";
        } else if ("/api/login".equals(endpoint)) {
            return "login";
        } else if ("/api/admin".equals(endpoint)) {
            return "admin";
        }
        return "default";
    }
}