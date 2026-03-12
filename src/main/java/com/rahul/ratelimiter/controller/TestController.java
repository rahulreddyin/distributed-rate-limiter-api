package com.rahul.ratelimiter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TestController {

    @GetMapping("/api/data")
    public Map<String, String> getData(@RequestHeader("X-User-Id") String userId) {
        return Map.of(
                "endpoint", "/api/data",
                "message", "Data endpoint accessed successfully",
                "userId", userId
        );
    }

    @GetMapping("/api/login")
    public Map<String, String> login(@RequestHeader("X-User-Id") String userId) {
        return Map.of(
                "endpoint", "/api/login",
                "message", "Login endpoint accessed successfully",
                "userId", userId
        );
    }

    @GetMapping("/api/admin")
    public Map<String, String> admin(@RequestHeader("X-User-Id") String userId) {
        return Map.of(
                "endpoint", "/api/admin",
                "message", "Admin endpoint accessed successfully",
                "userId", userId
        );
    }
}