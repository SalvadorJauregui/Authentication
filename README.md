# Java Authentication Showcase

A clean, professional showcase implementation of a Java REST API authentication system. This project is designed to reflect modern authentication architecture, security patterns, and role-based access control best practices.

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
- Refresh token issuance and validation
- Role-based access control using Spring Security annotations
- Clear separation between controllers, services, repositories, and security configuration
- Centralized exception handling with structured API error responses

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

- The project is structured to show authentication architecture and REST API design.
- The code is organized to look professional and complete for a GitHub showcase.
