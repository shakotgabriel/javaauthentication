JWT Authentication & Role-Based Access Control API
This project is a Spring Boot REST API that implements JWT-based authentication and role-based authorization (RBAC) using Spring Security.

Features
User registration and login
JWT token generation and validation
Stateless authentication with bearer tokens
Role model with ROLE_USER and ROLE_ADMIN
Protected endpoints using Spring Security
Input validation for auth requests
H2 profile for local development and MySQL profile for database-backed environments
Integration tests for register/login flows
Tech Stack
Java 17
Spring Boot 4
Spring Security
Spring Data JPA
H2 / MySQL
Maven
JJWT

API Endpoints
Public
GET /
Returns a basic API status message.
GET /health
Returns service health.
POST /api/auth/register
Registers a user and returns JWT response.
POST /api/auth/login
Authenticates credentials and returns JWT response.
Protected
Any non-public endpoint requires:
Header: Authorization: Bearer <token>
Auth Flow
Register with name, email, and password.
Receive JWT token in the response.
Send token in Authorization header for protected endpoints.
Role checks are enforced by Spring Security.

Local Run
Server starts on:

http://localhost:8080

Environment Variables
Set these in production:

JWT_SECRET (required)
JWT_EXPIRATION_MS (optional, default: 3600000)
For MySQL profile, also set:

DB_USERNAME
DB_PASSWORD
