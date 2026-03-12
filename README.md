# Distributed Rate Limiter API

A scalable **distributed rate limiting service** built using **Spring Boot, Redis, and Docker**.
This system prevents API abuse by controlling how many requests a user or client can make within a defined time window.

Designed for **microservices architectures and high-traffic APIs**.

---

## System Architecture

Client → API Gateway → Rate Limiter Service → Redis
![Architecture](docs/architecture.png)

Redis acts as the **central distributed store** to track request counts across multiple application instances.

---

## Features

• Distributed rate limiting using Redis
• Token bucket / fixed window rate limiting strategy
• REST API for request validation
• Dockerized deployment
• Configurable request limits
• Scalable across multiple service instances

---

## Tech Stack

Backend
• Java
• Spring Boot

Infrastructure
• Redis
• Docker
• Docker Compose

Tools
• Maven
• Git

---

## How Rate Limiting Works

1. Client sends request to API
2. Service checks Redis for request count
3. If request limit exceeded → return HTTP 429
4. If within limit → allow request

This ensures fair usage and protects backend services from overload.

---

## API Endpoints

### Check Rate Limit

POST /api/rate-limit/check

Request Example

{
"clientId": "user123"
}

Response

{
"allowed": true,
"remainingRequests": 4
}

---

## Running Locally

### Start Redis

docker-compose up -d

### Build application

./mvnw clean package

### Run application

java -jar target/ratelimiter-0.0.1-SNAPSHOT.jar

Server runs on

http://localhost:8080

---

## Docker Deployment

Build image

docker build -t rate-limiter .

Run container

docker run -p 8080:8080 rate-limiter

---

## Future Improvements

• Sliding window rate limiting
• Prometheus metrics integration
• Grafana monitoring dashboards
• Kubernetes deployment
• API Gateway integration

---

## Author

Rahul Reddy Puli

Full Stack Software Engineer focused on scalable backend systems, distributed architectures, and cloud applications.
