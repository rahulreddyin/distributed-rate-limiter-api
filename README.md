# Distributed Rate Limiter API

A scalable **distributed rate limiting service** built using **Spring Boot, Redis, and Docker**.
This system prevents API abuse by controlling how many requests a user or client can make within a defined time window.

It is designed for **microservices architectures and high-traffic APIs**, where multiple service instances must enforce the same rate-limiting rules consistently.

---

# System Architecture

![Architecture](docs/architecture.png)

### Request Flow

Client → Spring Boot API → Redis → Response

Redis acts as a **central distributed store** that tracks request counts across all instances of the application.

---

# Features

• Distributed rate limiting using Redis
• Fixed window request throttling
• Per-user rate limiting via request headers
• REST APIs for request validation
• Dockerized Redis setup
• Integration tests for validating rate limit behavior
• Scalable backend design suitable for microservices environments

---

# Tech Stack

### Backend

* Java
* Spring Boot
* REST APIs

### Data Store

* Redis

### Infrastructure

* Docker
* Docker Compose

### Build Tools

* Maven
* Git

---

# How Rate Limiting Works

1. A client sends a request to the API.
2. The service checks Redis for the number of requests made by that user.
3. If the request count exceeds the configured limit → return **HTTP 429 (Too Many Requests)**.
4. If the request is within the limit → allow the request and increment the counter.

Redis ensures **consistent request counting across distributed instances**.

---

# API Endpoints

## Access Data Endpoint

Request

```http
GET /api/data
X-User-Id: user123
```

Response

```json
{
  "endpoint": "/api/data",
  "message": "Data endpoint accessed successfully",
  "userId": "user123"
}
```

---

## Login Endpoint

Request

```http
GET /api/login
X-User-Id: user123
```

Response

```json
{
  "endpoint": "/api/login",
  "message": "Login endpoint accessed successfully",
  "userId": "user123"
}
```

---

## Admin Endpoint

Request

```http
GET /api/admin
X-User-Id: user123
```

Response

```json
{
  "endpoint": "/api/admin",
  "message": "Admin endpoint accessed successfully",
  "userId": "user123"
}
```

---

## Rate Limit Exceeded

Response

```json
{
  "timestamp": "2026-03-11T20:30:00",
  "status": 429,
  "error": "Too Many Requests",
  "message": "Rate limit exceeded. Try again later.",
  "path": "/api/admin"
}
```

---

# Check Rate Limit Status

Request

```http
GET /admin/ratelimit/status?userId=user123&endpoint=/api/data
```

Response

```json
{
  "userId": "user123",
  "endpoint": "/api/data",
  "currentRequests": 3,
  "maxRequests": 5,
  "remainingRequests": 2,
  "windowSeconds": 60
}
```

---

# Run Locally

## Clone Repository

```bash
git clone https://github.com/rahulreddyin/distributed-rate-limiter-api.git
cd distributed-rate-limiter-api
```

---

## Start Redis

```bash
docker compose up -d redis
```

---

## Run Spring Boot Application

### Windows

```powershell
.\mvnw.cmd spring-boot:run
```

### macOS / Linux

```bash
./mvnw spring-boot:run
```

---

## Application URL

```
http://localhost:8080
```

Example request:

```bash
curl -H "X-User-Id: user123" http://localhost:8080/api/data
```

---

# Testing

Integration tests verify:

• endpoint-specific rate limiting
• Redis request tracking
• HTTP 429 responses when limits are exceeded

Run tests:

### Windows

```powershell
.\mvnw.cmd test
```

### macOS / Linux

```bash
./mvnw test
```

---

# Future Improvements

• Implement **Token Bucket algorithm**
• Add **Sliding Window rate limiting strategy**
• Use **Redis Lua scripts for atomic operations**
• Add **Prometheus metrics monitoring**
• Integrate **Grafana dashboards for traffic visualization**
• Stream rate-limit events using **Kafka**
• Deploy service to **AWS / Kubernetes**
• Add authentication and API keys

---

# Author

Rahul Reddy Puli

Full Stack Software Engineer focused on **distributed systems, scalable backend architectures, and cloud-native applications**.
