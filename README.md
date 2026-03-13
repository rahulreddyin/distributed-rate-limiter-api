# Distributed Rate Limiter API

A production-style **distributed rate limiting service** built using **Spring Boot, Redis, Docker, Prometheus, and Grafana**.

The service protects APIs from abuse by limiting the number of requests a user can make within a configured time window.

This project demonstrates **backend system design, distributed caching, observability, and containerized deployment** similar to real-world API infrastructure used in large-scale systems.

---

# Architecture

![System Architecture](docs/architecture.png)

### Architecture Flow

1. Client sends a request to the API
2. API extracts the `X-User-Id` header
3. `RateLimiterService` checks Redis for the request counter
4. Redis executes an **atomic Lua script** to increment the counter
5. TTL is applied to enforce the request window
6. If the request exceeds the configured limit → request blocked
7. Prometheus scrapes metrics exposed by the application
8. Grafana visualizes the system metrics and request patterns

---

# Features

- Distributed rate limiting using Redis
- Per-user and per-endpoint request throttling
- Configurable rate limit windows
- **Atomic Redis Lua script for concurrency-safe counters**
- Docker-based deployment
- Prometheus metrics integration
- Grafana monitoring dashboards
- Production-style backend architecture

---

# Tech Stack

| Component | Technology |
|----------|-----------|
| Backend | Spring Boot |
| Cache | Redis |
| Build Tool | Maven |
| Containerization | Docker |
| Monitoring | Prometheus |
| Visualization | Grafana |
| Metrics | Micrometer |

---

# Project Structure

```
ratelimiter
│
├── src/main/java/com/rahul/ratelimiter
│   ├── controller
│   │   └── RateLimitController.java
│   │
│   ├── service
│   │   └── RateLimiterService.java
│   │
│   ├── config
│   │   └── RedisScriptConfig.java
│   │
│   ├── model
│   │   └── RateLimitProperties.java
│   │
│   └── RatelimiterApplication.java
│
├── src/main/resources
│   ├── application.properties
│   └── scripts
│       └── rate_limiter.lua
│
├── docs
│   ├── architecture.png
│   └── grafana-dashboard.png
│
├── monitoring
│   ├── prometheus.yml
│   └── grafana-dashboard.json
│
├── docker-compose.yml
├── Dockerfile
├── pom.xml
└── README.md
```

---

# Rate Limiting Configuration

Example configuration inside `application.properties`:

```
ratelimiter.windowSeconds=60

ratelimiter.limits.data=10
ratelimiter.limits.login=5
ratelimiter.limits.admin=2
```

| Endpoint | Limit | Window |
|---------|------|--------|
| /api/data | 10 requests | 60 seconds |
| /api/login | 5 requests | 60 seconds |
| /api/admin | 2 requests | 60 seconds |

---

# Redis Atomic Rate Limiting

The rate limiter uses a **Redis Lua script** to ensure atomic updates to request counters.

This prevents race conditions when multiple requests hit the same user key simultaneously.

### Lua Script

```
src/main/resources/scripts/rate_limiter.lua
```

```lua
local current = redis.call("INCR", KEYS[1])

if current == 1 then
    redis.call("EXPIRE", KEYS[1], ARGV[1])
end

return current
```

### Why Lua Scripts?

Benefits of using Redis Lua scripts:

- Atomic operations
- No race conditions
- Reduced network round trips
- Production-grade distributed rate limiting

---

# Running the Application

### Clone Repository

```
git clone https://github.com/rahulreddyin/distributed-rate-limiter-api.git
cd distributed-rate-limiter-api
```

---

# Start Redis and Monitoring Stack

```
docker compose up -d
```

Verify running containers:

```
docker ps
```

Expected services:

| Service | Port |
|------|------|
| Spring Boot API | 8080 |
| Redis | 6379 |
| Prometheus | 9090 |
| Grafana | 3000 |

---

# Start Spring Boot Application (Local Development)

```
./mvnw spring-boot:run
```

Application runs at:

```
http://localhost:8080
```

---

# API Usage

All requests must include the header:

```
Header: X-User-Id
```

---

# Example Request

```
GET /api/data
```

### Request

```
GET http://localhost:8080/api/data
Header: X-User-Id: user123
```

### Response

```json
{
  "message": "Request allowed",
  "endpoint": "/api/data"
}
```

---

# Rate Limit Exceeded Example

Third request within window:

```json
{
  "error": "Too Many Requests",
  "message": "Rate limit exceeded"
}
```

HTTP Status

```
429 Too Many Requests
```

---

# Observability

The service exposes **Prometheus-compatible metrics** through Spring Boot Actuator.

Metrics endpoint:

```
http://localhost:8080/actuator/prometheus
```

Prometheus collects:

- JVM metrics
- HTTP request metrics
- Redis command metrics
- Custom rate limiter metrics

---

# Monitoring Dashboard (Grafana)

The system includes a full **observability stack using Prometheus and Grafana**.

### Example Dashboard

<p align="center">
<img src="./docs/grafana-dashboard.png" width="900">
</p>

The dashboard shows:

- allowed requests
- blocked requests
- API request throughput
- system performance metrics

---

# Key Metrics

### Allowed Requests

```
ratelimiter_requests_allowed_total
```

### Blocked Requests

```
ratelimiter_requests_blocked_total
```

### API Throughput

```
sum(rate(http_server_requests_seconds_count{uri="/api/data"}[5m]))
```

---

# Prometheus Queries

Example queries used in monitoring dashboards.

Allowed requests:

```
ratelimiter_requests_allowed_total
```

Blocked requests:

```
ratelimiter_requests_blocked_total
```

API request rate:

```
sum(rate(http_server_requests_seconds_count{uri="/api/data"}[5m]))
```

---

# Accessing Monitoring Tools

Prometheus UI

```
http://localhost:9090
```

Grafana dashboard

```
http://localhost:3000
```

Login:

```
Username: admin
Password: admin
```

---

# Docker Deployment

Start the full stack:

```
docker compose up --build
```

Services started:

| Service | Port |
|------|------|
| Spring Boot API | 8080 |
| Redis | 6379 |
| Prometheus | 9090 |
| Grafana | 3000 |

---

# Example Rate Limiter Workflow

Client request

```
GET /api/admin
Header: X-User-Id: admin123
```

Redis key generated

```
rate_limit:admin123:/api/admin
```

Redis Lua script increments counter.

If limit exceeded → request blocked.

---

# System Design Concepts Demonstrated

This project demonstrates:

- distributed rate limiting
- Redis atomic counters
- Lua script execution in Redis
- API request throttling
- observability using Prometheus
- monitoring dashboards with Grafana
- containerized backend services
- scalable backend architecture

---

# Future Improvements

Possible enhancements:

- Sliding window rate limiter
- Token bucket algorithm
- API gateway integration
- Kubernetes deployment
- distributed multi-instance testing

---

# Author

Rahul Reddy

GitHub

```
https://github.com/rahulreddyin
```

---