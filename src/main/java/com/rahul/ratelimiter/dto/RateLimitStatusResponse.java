package com.rahul.ratelimiter.dto;

public class RateLimitStatusResponse {

    private String userId;
    private String endpoint;
    private long currentRequests;
    private int maxRequests;
    private int remainingRequests;
    private int windowSeconds;

    public RateLimitStatusResponse() {
    }

    public RateLimitStatusResponse(String userId, String endpoint, long currentRequests, int maxRequests, int remainingRequests, int windowSeconds) {
        this.userId = userId;
        this.endpoint = endpoint;
        this.currentRequests = currentRequests;
        this.maxRequests = maxRequests;
        this.remainingRequests = remainingRequests;
        this.windowSeconds = windowSeconds;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public long getCurrentRequests() {
        return currentRequests;
    }

    public void setCurrentRequests(long currentRequests) {
        this.currentRequests = currentRequests;
    }

    public int getMaxRequests() {
        return maxRequests;
    }

    public void setMaxRequests(int maxRequests) {
        this.maxRequests = maxRequests;
    }

    public int getRemainingRequests() {
        return remainingRequests;
    }

    public void setRemainingRequests(int remainingRequests) {
        this.remainingRequests = remainingRequests;
    }

    public int getWindowSeconds() {
        return windowSeconds;
    }

    public void setWindowSeconds(int windowSeconds) {
        this.windowSeconds = windowSeconds;
    }
}