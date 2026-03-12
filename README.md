# Distributed Rate Limiter API

A production-style **distributed rate limiting service** built using **Spring Boot, Redis, Docker, Prometheus, and Grafana**.

The service protects APIs from abuse by limiting the number of requests a user can make within a configured time window.

This project demonstrates **backend system design, distributed caching, observability, and containerized deployment**.

---

# Architecture

![System Architecture](docs/architecture.png)

### Architecture Flow

1. Client sends a request to the API
2. API extracts the `X-User-Id` header
3. `RateLimiterService` checks Redis for the request counter
4. Redis stores request counters with TTL expiration
5. If the request exceeds the limit → request blocked
6. Prometheus collects metrics for monitoring
7. Grafana visualizes metrics using dashboards

---

# Features

- Distributed rate limiting using Redis
- Per-user and per-endpoint request throttling
- Configurable rate limit windows
- Docker-based local deployment
- Prometheus metrics and observability
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
│   ├── model
│   │   └── RateLimitProperties.java
│   │
│   └── RatelimiterApplication.java
│
├── src/main/resources
│   └── application.properties
│
├── docs
│   ├── architecture.png
│   └── grafana-dashboard.png
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

# Running the Application

### Clone Repository

```
git clone https://github.com/rahulreddyin/distributed-rate-limiter-api.git
cd distributed-rate-limiter-api
```

---

# Start Redis

```
docker compose up -d redis
```

Verify Redis container:

```
docker ps
```

Expected output:

```
CONTAINER ID   IMAGE   PORTS
redis          redis   6379->6379
```

---

# Start Spring Boot Application

```
./mvnw spring-boot:run
```

Application runs on:

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

### Endpoint

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

# Example Admin Endpoint

```
GET http://localhost:8080/api/admin
Header: X-User-Id: admin123
```

### Request 1

```json
{
  "message": "Request allowed",
  "endpoint": "/api/admin"
}
```

### Request 2

```json
{
  "message": "Request allowed",
  "endpoint": "/api/admin"
}
```

---

# Rate Limit Exceeded Example

Third request within the configured window:

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

# Postman Testing Example

Send multiple requests quickly:

```
GET /api/admin
Header: X-User-Id: admin123
```

Expected results:

| Request | Result |
|-------|-------|
| 1 | Allowed |
| 2 | Allowed |
| 3 | Blocked |

---

# Observability

The service exposes **Prometheus-compatible metrics**.

Metrics endpoint:

```
http://localhost:8080/actuator/prometheus
```

Prometheus collects metrics including:

- application performance
- API request statistics
- JVM metrics
- system resource usage
- Redis command latency

---

## Configuration

The service supports environment-based configuration for local, Docker, and cloud deployments.

Example environment variables:

```env
SPRING_DATA_REDIS_HOST=redis
SPRING_DATA_REDIS_PORT=6379
SERVER_PORT=8080
```

## Health Check

The application exposes a health endpoint for service monitoring:

```text
http://localhost:8080/actuator/health
```

# Monitoring Dashboard (Grafana)

The system includes a complete **observability stack using Prometheus and Grafana** to monitor application performance and rate limiting behavior.

### Example Grafana Dashboard

![Grafana Dashboard](docs/grafana-dashboard.png)

The dashboard provides real-time insight into:

- allowed requests
- blocked requests
- API request throughput
- application performance metrics

---

### Metrics Visualized

**Allowed Requests**

```
ratelimiter_requests_allowed_total
```

Counts the number of requests that successfully passed the rate limiter.

---

**Blocked Requests**

```
ratelimiter_requests_blocked_total
```

Counts requests rejected with **HTTP 429 (Too Many Requests)**.

---

**API Requests Per Second**

```
sum(rate(http_server_requests_seconds_count{uri="/api/data"}[5m]))
```

Displays API request throughput for the `/api/data` endpoint.

---

### Monitoring Architecture

```
Client
   │
   ▼
Spring Boot API
   │
   ▼
Redis (Rate Limiting State)
   │
   ▼
Prometheus (Metrics Collection)
   │
   ▼
Grafana (Monitoring Dashboard)
```

---

### Accessing the Monitoring Stack

Grafana dashboard:

```
http://localhost:3000
```

Default credentials:

```
Username: admin
Password: admin
```

Prometheus metrics endpoint:

```
http://localhost:8080/actuator/prometheus
```

---

# Custom Metrics

| Metric | Description |
|------|-------------|
| ratelimiter_requests_allowed_total | Total allowed requests |
| ratelimiter_requests_blocked_total | Total blocked requests |

Example output:

```
ratelimiter_requests_allowed_total 5
ratelimiter_requests_blocked_total 2
```

---

# Additional Metrics

Prometheus automatically exposes system metrics including:

```
jvm_memory_used_bytes
system_cpu_usage
http_server_requests_seconds
process_cpu_usage
```

These metrics help monitor:

- JVM memory usage
- CPU utilization
- HTTP request performance
- service health

---

# Docker Deployment

Start the full stack:

```
docker compose up --build
```

Services started:

| Service | Port |
|-------|------|
| Spring Boot API | 8080 |
| Redis | 6379 |
| Prometheus | 9090 |
| Grafana | 3000 |

---

# Example Rate Limiter Workflow

Client request:

```
GET /api/admin
Header: X-User-Id: admin123
```

Redis key generated:

```
rate_limit:admin123:/api/admin
```

Redis increments request counter.

If the configured limit is exceeded → request blocked.

---

# System Design Concepts Demonstrated

This project demonstrates:

- distributed rate limiting
- Redis-based caching
- time-window request throttling
- observability using Prometheus
- containerized backend services
- scalable backend architecture
- monitoring with Grafana dashboards

---

# Future Improvements

Possible enhancements:

- Token Bucket rate limiting
- Sliding Window rate limiter
- Kubernetes deployment
- API Gateway integration
- multi-instance distributed testing

---

# Author

Rahul Reddy

GitHub

```
https://github.com/rahulreddyin
```