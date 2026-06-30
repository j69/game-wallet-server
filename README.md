Wallet-Server that provides an HTTP API for game-engine and manages customers’ gaming funds,
enabling games to be purchased and winnings to be paid out.

Made with Kotlin, Graddle, Spring Boot, PostgreSQL, Docker, JWT.

This service supports idempotent transactions, JWT-based authentication,
PostgreSQL persistence, and integration testing.

Additionally there is some basic validation and logging.

The application follows some layered architecture:
* Controller = handles HTTP requests and responses, DTO mapping
* Service = contains Wallet business logic, transactional idempotency
* Repository = uses Spring Data JPA, data storage operations 
* Security = protects endpoints with Bearer <token>, Stateless JWT authentication

## How to run:
* docker-compose up -d
* ./gradlew bootRun 

The service will start on:
http://localhost:8080/

## You can check Database in terminal:
* docker exec -it wallet-db psql -U wallet_user -d wallet
* \dt

## There is 2 PostgreSQL tables

- players

Stores player name and wallet balance information.

| Column  | Type    | Description              |
|---------| ------- | ------------------------ |
| pid     | VARCHAR | Unique player identifier |
| name    | VARCHAR | Player name              |
| balance | BigDecimal | Current wallet balance   |

- wallet_transactions

Stores all wallet operations.

| Column    | Type                         | Description                  |
|-----------|------------------------------| ---------------------------- |
| uid       | VARCHAR                      | Unique transaction id |
| pid       | VARCHAR                      | Reference to player          |
| type      | ENUM (PURCHASE, WIN) | Transaction type             |
| amount    | BigDecimal                      | Transaction amount           |
| timestamp | TIMESTAMP                    | When transaction happened    |

there is DataInitializer which Creates Test Player with balance 10.00

## You can also check buisness-logic (incoming requests) in log file:
logs/penny-service.log

## You can run Integration tests
./gradlew test

# You can make API calls:
Detailed API documentation is in the API.md file

## Step 1 - get token from Public API - Returns JWT token.

by default its uses clientId is "VeikGameEngine" and secret is "blackjack"

```
POST http://localhost:8080/api/login
{
    "clientId": "VeikGameEngine",
    "secret": "blackjack"
}
```

## Step 2 - The game engine makes a purchase for a game (Protected API)
Using Authtoken make a call to
```
POST http://localhost:8080/api/purchase
header "Authorization: Bearer PUT_AUTHTOKEN_HERE"
{
    "uid": "game1",
    "pid": "player1",
    "sum": 2.0
}
```
And Responce will be:
```
{
    "status": "success",
    "balance": 8.00
}
```

## Step 3 - The game engine makes a purchase for a game (Protected API)
Using Authtoken make a call to
```
POST http://localhost:8080/api/win
header "Authorization: Bearer PUT_AUTHTOKEN_HERE"
{
    "uid": "game2",
    "pid": "player1",
    "sum": 2.0
}
```
And Responce will be:
```
{
    "status": "success",
    "balance": 10.00
}
```
