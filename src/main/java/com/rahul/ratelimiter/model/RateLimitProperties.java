package com.rahul.ratelimiter.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "ratelimiter")
public class RateLimitProperties {

    private int windowSeconds;
    private Map<String, Integer> limits = new HashMap<>();

    public int getWindowSeconds() {
        return windowSeconds;
    }

    public void setWindowSeconds(int windowSeconds) {
        this.windowSeconds = windowSeconds;
    }

    public Map<String, Integer> getLimits() {
        return limits;
    }

    public void setLimits(Map<String, Integer> limits) {
        this.limits = limits;
    }
}