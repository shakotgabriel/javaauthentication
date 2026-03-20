# Spring Boot JWT Auth API

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.4-6DB33F)
![Build](https://img.shields.io/badge/Build-Passing-brightgreen)

A production-style Spring Boot REST API for JWT authentication and role-based authorization (RBAC).

## Why This Project

- Demonstrates a complete JWT auth flow from register and login to protected routes.
- Uses stateless Spring Security with role enforcement.
- Includes automated integration tests for auth behavior.
- Supports H2 for local development and MySQL profile for external DB usage.

## Features

- User registration and login
- JWT token generation and validation
- Role model: ROLE_USER and ROLE_ADMIN
- Public and protected routes with Spring Security configuration
- Bean validation for auth payloads
- Integration tests using MockMvc

## Tech Stack

- Java 17
- Spring Boot 4.0.4
- Spring Security
- Spring Data JPA
- H2 and MySQL
- Maven
- JJWT

## Project Structure

- `jwtauthapi/src/main/java/com/shakot/jwtauthapi/controller` : REST controllers
- `jwtauthapi/src/main/java/com/shakot/jwtauthapi/service` : auth and user services
- `jwtauthapi/src/main/java/com/shakot/jwtauthapi/security` : JWT and security configuration
- `jwtauthapi/src/main/java/com/shakot/jwtauthapi/model` : domain entities and roles
- `jwtauthapi/src/main/java/com/shakot/jwtauthapi/repository` : JPA repositories
- `jwtauthapi/src/test/java/com/shakot/jwtauthapi` : integration and context tests

## Quick Start

### 1. Clone and run

```bash
git clone https://github.com/shakotgabriel/javaauthentication.git
cd javaauthentication/jwtauthapi
./mvnw spring-boot:run
```

The API starts at:

- http://localhost:8080

### 2. Register user

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"user1@example.com","password":"Password123"}'
```

### 3. Login user

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user1@example.com","password":"Password123"}'
```

## API Endpoints

### Public

- `GET /` : basic API status message
- `GET /health` : service health check
- `POST /api/auth/register` : create a user account and return JWT
- `POST /api/auth/login` : authenticate and return JWT

### Protected

Any non-public endpoint requires:

- `Authorization: Bearer <token>`

## Environment Variables

Set these in production:

- `JWT_SECRET` (required)
- `JWT_EXPIRATION_MS` (optional, default `3600000`)

For MySQL profile:

- `DB_USERNAME`
- `DB_PASSWORD`

## Run Tests

```bash
cd jwtauthapi
./mvnw test
```

## Status

Current implementation includes:

- Register/login JWT flow
- Security filter chain and JWT filter
- Role-aware user model
- Integration tests for auth success and failure paths

Planned next improvements:

- Add admin bootstrap strategy for non-local environments
- Add refresh token flow
- Add OpenAPI/Swagger docs
- Add Dockerfile and compose profile

## Contributing

Contributions are welcome.

- Open an issue with bug details or feature proposal.
- Fork the repo and create a feature branch.
- Add tests for behavior changes.
- Submit a PR with a clear summary.

## License

Add a LICENSE file before publishing if you want explicit open-source usage terms.
