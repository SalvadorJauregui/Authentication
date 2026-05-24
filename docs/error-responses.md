# API Error Responses

This document describes common API error response shapes produced by the application and includes example curl calls demonstrating error scenarios.

## Error Response Shape

All errors are returned using the following JSON structure:

```json
{
  "timestamp": "2026-05-23T12:34:56.789",
  "status": 400,
  "error": "Bad Request",
  "message": "Detailed message explaining the problem"
}
```

## Examples

- Validation error (missing password on register):

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"jdoe","email":"jdoe@example.com"}'
```

Response (400):

```json
{
  "timestamp": "...",
  "status": 400,
  "error": "Bad Request",
  "message": "password: Password is required"
}
```

- Invalid credentials (login):

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"unknown@example.com","password":"x"}'
```

Response (404):

```json
{
  "timestamp": "...",
  "status": 404,
  "error": "Not Found",
  "message": "Invalid login credentials"
}
```

- Using revoked refresh token:

```bash
curl -X POST http://localhost:8080/api/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{"refreshToken":"REV_TOKEN"}'
```

Response (400):

```json
{
  "timestamp": "...",
  "status": 400,
  "error": "Bad Request",
  "message": "Refresh token is revoked or expired"
}
```
