# Java Authentication Project

A clean, professional implementation of a Java REST API authentication system. This project is designed to reflect modern authentication architecture, security patterns, and role-based access control best practices.

## Project Architecture

- `pom.xml`: Maven build and dependency configuration
- `src/main/java/com/auth`: application source code
- `src/main/java/com/auth/controller`: REST controllers for authentication and protected user endpoints
- `src/main/java/com/auth/service`: core authentication and user service logic
- `src/main/java/com/auth/repository`: repository interfaces and an in-memory repository implementation
- `src/main/java/com/auth/security`: JWT provider, request filter, and Spring Security configuration
- `src/main/java/com/auth/model`: domain models and role definitions
- `src/main/java/com/auth/dto`: request and response payload objects
- `src/main/java/com/auth/exception`: centralized exception handling and API error responses
- `src/main/resources/application.properties`: runtime configuration

## Project Structure

Top-level project layout and notable files:

- `pom.xml` — Maven build and test configuration
- `src/main/java/com/auth` — application sources
  - `AuthApplication.java` — application entrypoint
  - `DataLoader.java` — creates an initial admin user on startup
  - `config/` — Spring Security configuration
  - `controller/` — REST controllers (`AuthController`, `UserController`)
  - `service/` — business logic (`AuthService`, `UserService`)
  - `repository/` — repository interfaces and in-memory implementations
  - `security/` — `JwtProvider`, `JwtAuthenticationFilter`
  - `model/` — `User`, `RefreshToken`, `Role`
  - `dto/` — request/response payloads
  - `exception/` — centralized exception handling
- `src/main/resources/log4j2.xml` — logging configuration
- `docs/error-responses.md` — API error shapes and sample curl flows
- `src/test/java` — unit tests for services and repositories

## What this project does

This project demonstrates a realistic authentication REST API with the following behavior:

- User registration and login flows with input validation and structured responses.
- JWT-style access tokens and refresh tokens: access tokens are issued for short-lived access, refresh tokens are persisted and managed for session continuation.
- Refresh-token lifecycle management: refresh tokens are saved on login, validated and revoked on refresh, and can be explicitly revoked via a logout endpoint.
- Role-based access control (RBAC): `USER` and `ADMIN` roles control access to protected endpoints.
- In-memory repositories are provided for quick local testing; these can be replaced by DB-backed implementations with minimal code changes.
- Startup convenience: a seeded admin account (`admin@example.com` / `Admin123!`) is created for manual testing.

The intent is to showcase clear separation of concerns between controllers, services, repositories and security, and to provide practical examples of token management and RBAC.

## API Endpoints

### Authentication Endpoints

- `POST /api/auth/register`
  - Registers a new user and assigns the `USER` role.
  - Example:
    ```bash
    curl -X POST http://localhost:8080/api/auth/register \
      -H "Content-Type: application/json" \
      -d '{"username":"jdoe","email":"jdoe@example.com","password":"Secret123"}'
    ```

- `POST /api/auth/login`
  - Authenticates a user and returns JWT access and refresh tokens.
  - Example:
    ```bash
    curl -X POST http://localhost:8080/api/auth/login \
      -H "Content-Type: application/json" \
      -d '{"email":"jdoe@example.com","password":"Secret123"}'
    ```

- `POST /api/auth/refresh`
  - Refreshes an access token using the refresh token.
  - Example:
    ```bash
    curl -X POST http://localhost:8080/api/auth/refresh \
      -H "Content-Type: application/json" \
      -d '{"refreshToken":"REFRESH_TOKEN_HERE"}'
    ```

### Protected User Endpoints

- `GET /api/users/me`
  - Returns the authenticated user's profile. Requires `USER` or `ADMIN` role.
  - Example:
    ```bash
    curl -X GET http://localhost:8080/api/users/me \
      -H "Authorization: Bearer ACCESS_TOKEN_HERE"
    ```

- `GET /api/users/admin`
  - Demonstrates role-based access control for `ADMIN` users.
  - Example:
    ```bash
    curl -X GET http://localhost:8080/api/users/admin \
      -H "Authorization: Bearer ACCESS_TOKEN_HERE"
    ```

## Security Design

- Stateless JWT authentication workflow
- Refresh token issuance, persistence and revocation strategy
- Role-based access control using Spring Security annotations
- Clear separation between controllers, services, repositories, and security configuration
- Centralized exception handling with structured API error responses (see `docs/error-responses.md`)

## How to Run

1. Install Java 17 and Maven.
2. Open the project root in your terminal.
3. Build the project:
  ```bash
  mvn clean package
  ```
4. Run the application:
  ```bash
  mvn spring-boot:run
  ```
5. Use the API examples above to exercise the authentication flows.

## Notes

- The project is structured to demonstrate authentication architecture and REST API design.
- The code is organized to look professional and complete for a GitHub project.

## Docs

- Error responses and example curl flows: `docs/error-responses.md`
