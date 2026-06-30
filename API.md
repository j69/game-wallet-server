# Wallet Service HTTP API

This document describes the HTTP interface between the Game Engine and the Wallet Service.

The API is stateless and secured using JWT Bearer authentication.

---

# Base URL

```text
http://localhost:8080
```

---

# Content Type

All requests and responses use:

```http
Content-Type: application/json
```

---

# Authentication

## POST /api/login

Returns JWT token for accessing protected endpoints.

### Request

```http
POST /api/login
```

### Request Body

```json
{
  "clientId": "someID",
  "secret": "1234"
}
```

### Success Response

```http
200 OK
```

```json
{
  "token": "jwt-token"
}
```

### Error Response

```http
401 Unauthorized
```

```json
{
  "status": "error",
  "message": "Invalid credentials"
}
```

---

# Purchase a game for Player

## POST /api/purchase

Deducts funds from the player's wallet balance.

The operation is idempotent using transaction ID (`uid`).

### Authentication

```http
Authorization: Bearer <token>
```

### Request

```http
POST /api/purchase
```

### Request Body

```json
{
  "uid": "tx123",
  "pid": "player1",
  "sum": 2.0
}
```

### Parameters

| Field | Type | Description |
|---|---|---|
| uid | string | Unique transaction identifier |
| pid | string | Unique player identifier |
| sum | decimal | Amount to purchase |

### Success Response

```http
200 OK
```

```json
{
  "balance": 8.0
}
```

### Error Responses

#### Insufficient funds

```http
400 Bad Request
```

```json
{
  "status": "error",
  "message": "Not enough money"
}
```

#### Player not found

```http
404 Not Found
```

```json
{
  "status": "error",
  "message": "Player not found"
}
```

#### Unauthorized

```http
401 Unauthorized
```

```json
{
  "status": "error",
  "message": "Unauthorized"
}
```

---

# Credit Winnings

## POST /api/win

Credits winnings to the player's wallet balance.

The operation is idempotent using transaction ID (`uid`).

### Authentication

```http
Authorization: Bearer <token>
```

### Request

```http
POST /api/win
```

### Request Body

```json
{
  "uid": "win123",
  "pid": "player1",
  "sum": 2.0
}
```

### Parameters

| Field | Type | Description |
|---|---|---|
| uid | string | Unique transaction identifier |
| pid | string | Unique player identifier |
| sum | decimal | Winning amount |

### Success Response

```http
200 OK
```

```json
{
  "balance": 10.0
}
```

### Error Responses

#### Player not found

```http
404 Not Found
```

```json
{
  "status": "error",
  "message": "Player not found"
}
```

#### Unauthorized

```http
401 Unauthorized
```

```json
{
  "status": "error",
  "message": "Unauthorized"
}
```

---

# 🔁 Idempotency

The API guarantees idempotent transaction processing.

If the same transaction ID (`uid`) is submitted multiple times:

- The transaction is processed only once
- Duplicate requests return the current wallet balance
- Double charging and double payout are prevented

This behavior allows safe retries in distributed systems.

---

# ⚠️ Validation Rules

| Field | Validation             |
|---|------------------------|
| uid | Must not be blank      |
| pid | Must not be blank      |
| sum | Must be greater than 2 |

Invalid requests return:

```http
400 Bad Request
```

---

# Transaction Model

Each transaction stores:

- Timestamp
- Player ID
- Transaction ID
- Transaction type (`PURCHASE` or `WIN`)
- Amount

---

# Security Notes

- JWT is used for service-to-service authentication
- API is stateless
- In production environments, HTTPS/TLS should be enabled for encrypted traffic

---

# Example Flow

## 1. Login

```http
POST /api/login
```

↓

Receive JWT token

↓

## 2. Purchase for player

```http
POST /api/purchase
Authorization: Bearer <token>
```

↓

Wallet balance decreases

↓

## 3. Credit winnings

```http
POST /api/win
Authorization: Bearer <token>
```

↓

Wallet balance increases
