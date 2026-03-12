package com.rahul.ratelimiter.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RateLimiterIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @BeforeEach
    void setUp() {
        redisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll();
    }

    @Test
    void shouldReturn429WhenRateLimitExceededForAdminEndpoint() {
        String url = "http://localhost:" + port + "/api/admin";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Id", "integration-test-user");

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> firstResponse = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        ResponseEntity<String> secondResponse = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        ResponseEntity<String> thirdResponse = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        assertThat(firstResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(secondResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(thirdResponse.getStatusCode()).isEqualTo(HttpStatus.TOO_MANY_REQUESTS);
    }
}