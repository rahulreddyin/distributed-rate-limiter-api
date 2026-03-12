# Distributed Rate Limiter & API Gateway

A backend systems project built with **Java, Spring Boot, Redis, Docker, and Docker Compose** that simulates API Gateway-style request throttling for high-traffic applications.

## Overview

This project implements a **Redis-backed rate limiter** that enforces per-user and per-endpoint request quotas. It uses Spring Boot interceptors to inspect incoming requests and block traffic when configured limits are exceeded.

The system supports:
- configurable request limits
- endpoint-specific throttling policies
- structured JSON error responses
- rate-limit status inspection
- Dockerized local deployment
- integration testing

## Features

- **Per-user rate limiting**
- **Per-endpoint throttling policies**
- **Redis-backed request counters**
- **Fixed-window rate limiting**
- **Structured JSON errors for HTTP 400 and 429**
- **Admin status endpoint for request monitoring**
- **Docker Compose setup for app + Redis**
- **Integration tests for throttling behavior**

## Tech Stack

- **Backend:** Java 17, Spring Boot
- **Cache / Rate Limit Store:** Redis
- **Build Tool:** Maven
- **Containerization:** Docker, Docker Compose
- **Testing:** JUnit, Spring Boot Test

## API Endpoints

### Protected endpoints

- `GET /api/data`
- `GET /api/login`
- `GET /api/admin`

Each endpoint has its own configurable request limit.

### Admin endpoint

- `GET /admin/ratelimit/status?userId=<userId>&endpoint=<endpoint>`

Example:

```http
GET /admin/ratelimit/status?userId=rahul123&endpoint=/api/data