# 🎮 Game Wallet Service

A backend wallet service that integrates with a game engine to handle in-game purchases and winnings.

The service supports idempotent transactions, JWT-based authentication, PostgreSQL persistence, and integration testing.

---

# 🚀 Features

- Charge player wallet for game purchases
- Credit winnings to player wallet
- Idempotent transaction processing
- JWT-based authentication
- PostgreSQL database persistence
- Input validation
- Global exception handling
- Integration tests using MockMvc
- Docker support

---

# 🧱 Architecture

The application follows a layered architecture:

```text
Controller → Service → Repository → Database
```

## Design Decisions

### Controller Layer
- Handles HTTP requests and responses
- Performs DTO mapping
- Contains no business logic

### Service Layer
- Contains business logic
- Handles wallet operations
- Ensures transactional consistency
- Implements idempotency

### Repository Layer
- Uses Spring Data JPA
- Handles persistence operations

### Security
- Stateless JWT authentication
- Protected endpoints require Bearer token

---

# 🔐 Authentication

The service uses JWT Bearer authentication.

## Public Endpoint

- `POST /api/login`

## Protected Endpoints

- `POST /api/charge`
- `POST /api/win`

Example header:

```http
Authorization: Bearer <token>
```

---

# 📡 API Overview

## Login

```http
POST /api/login
```

Returns JWT token.

---

## Charge Player

```http
POST /api/charge
```

Deducts funds from player balance.

---

## Credit Winnings

```http
POST /api/win
```

Credits winnings to player balance.

---

Detailed API documentation is available in:

```text
docs/API.md
```

---

# 🔁 Idempotency

The API guarantees idempotent transaction processing using transaction ID (`uid`).

If the same transaction ID is received multiple times:

- The transaction is processed only once
- Duplicate requests do not change balance
- Current balance is returned

This prevents double charging and double payout.

---

# ⚠️ Error Handling

Standard error response:

```json
{
  "status": "error",
  "message": "Insufficient funds"
}
```

## HTTP Status Codes

| Code | Description |
|---|---|
| 200 | Success |
| 400 | Validation or business error |
| 401 | Unauthorized |
| 404 | Resource not found |
| 500 | Internal server error |

---

# 🐳 Running the Application

## 1. Start PostgreSQL

```bash
docker-compose up -d
```

---

## 2. Run the application

```bash
./gradlew bootRun
```

The service will start on:

```text
http://localhost:8080
```

---

# 🧪 Running Tests

Run all tests:

```bash
./gradlew test
```

The project includes:

- Integration tests (MockMvc)
- JWT authentication tests
- Validation tests
- Idempotency tests
- Business flow tests

---

# 🛠 Technology Stack

- Kotlin
- Spring Boot
- Spring Security
- JWT (JJWT)
- PostgreSQL
- Spring Data JPA
- Hibernate
- MockMvc
- Docker
- Gradle

---

# 🧱 Database Model

## Player

Stores:

- Player ID
- Name
- Wallet balance

## Wallet Transaction

Stores:

- Timestamp
- Transaction ID
- Player ID
- Transaction type
- Amount

---

# 🔒 Security Notes

- JWT is used for service-to-service authentication
- The API is stateless
- HTTPS/TLS should be enabled in production environments
- Sensitive credentials should be stored in environment variables

---

# 📌 Project Goals

This project demonstrates:

- REST API design
- Backend architecture principles
- Transaction handling
- Idempotent API design
- Authentication and authorization
- Validation and error handling
- Integration testing
- Database persistence

---

# 🚀 Possible Future Improvements

- OpenAPI / Swagger documentation
- Role-based access control
- Refresh tokens
- Distributed locking for concurrency
- Metrics and tracing
- Rate limiting
- Container orchestratio
