# Distributed Calculator

An intentionally over-engineered calculator implementing Clean Architecture principles across distributed microservices. Each mathematical operation runs as a separate service in a different programming language.

## Architecture Overview

- **API Gateway** (Kotlin + Spring Boot) - Port 8080
- **Addition Service** (Kotlin + Spring Boot) - Port 8081
- **Subtraction Service** (Java + Spring Boot) - Port 8082
- **Multiplication Service** (Go + Gin) - Port 8083
- **Division Service** (Rust + Actix-web) - Port 8084
- **History Service** (Kotlin + Spring Boot + PostgreSQL) - Port 8085

## Quick Start

### Prerequisites
- Docker & Docker Compose
- Java 17 (for local development)
- Go 1.21+ (for local development)
- Rust 1.82+ (for local development)

### Running with Docker Compose

```bash
# Build JARs locally first (required for Kotlin/Java services)
cd api-gateway && JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64 ./gradlew build --no-daemon && cd ..
cd services/history-service && JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64 ./gradlew build --no-daemon && cd ../..

# Start all services
docker-compose up --build

# Start in background
docker-compose up -d --build

# Stop all services
docker-compose down

# View logs for specific services
docker-compose logs -f api-gateway
docker-compose logs -f history-service
docker-compose logs -f addition-service
```

### API Endpoints

#### Calculator Operations
```bash
# Addition
curl -X POST http://localhost:8080/api/v1/calculator/add \
  -H "Content-Type: application/json" \
  -d '{"operand1": "10.5", "operand2": "5.2"}'

# Subtraction
curl -X POST http://localhost:8080/api/v1/calculator/subtract \
  -H "Content-Type: application/json" \
  -d '{"operand1": "10.5", "operand2": "5.2"}'

# Multiplication
curl -X POST http://localhost:8080/api/v1/calculator/multiply \
  -H "Content-Type: application/json" \
  -d '{"operand1": "10.5", "operand2": "5.2"}'

# Division
curl -X POST http://localhost:8080/api/v1/calculator/divide \
  -H "Content-Type: application/json" \
  -d '{"operand1": "10.5", "operand2": "5.2"}'
```

#### History Service
```bash
# Get calculation history
curl http://localhost:8085/api/v1/history

# Get history by operation
curl http://localhost:8085/api/v1/history/operation/ADDITION

# Get operation statistics
curl http://localhost:8085/api/v1/history/stats/ADDITION
```

#### Health Checks
```bash
# API Gateway health
curl http://localhost:8080/actuator/health

# Service health checks
curl http://localhost:8081/health  # Addition
curl http://localhost:8082/health  # Subtraction
curl http://localhost:8083/health  # Multiplication
curl http://localhost:8084/health  # Division
curl http://localhost:8085/api/v1/history  # History (shows recent calculations)
```

### Testing with Postman

Import this collection or use these endpoints:

**Addition:**
```
POST http://localhost:8080/api/v1/calculator/add
Content-Type: application/json

{
  "operand1": "15.5",
  "operand2": "4.3"
}
```

**Response includes:**
- `result`: Calculation result
- `requestId`: Unique request identifier  
- `cached`: Whether result came from cache
- `processingTime`: Time taken for calculation

**View History:**
```
GET http://localhost:8085/api/v1/history
```

Shows all calculations with metadata including service name, timestamp, and processing time.

## Development

### Running Services Locally

Each service can be run independently:

```bash
# API Gateway
cd api-gateway
./gradlew bootRun

# Addition Service
cd services/addition-service
./gradlew bootRun

# Subtraction Service
cd services/subtraction-service
mvn spring-boot:run

# Multiplication Service
cd services/multiplication-service
go run main.go

# Division Service
cd services/division-service
cargo run
```

### Database Access

The system uses PostgreSQL for history persistence and Redis for caching:

**PostgreSQL Connection:**
- Host: localhost:5432
- Database: calculator_history
- User: calculator  
- Password: calculator123

**Redis Connection:**
- Host: localhost:6379
- No password required

**View Database Directly:**
```bash
# Connect to PostgreSQL
docker exec -it calculator-postgres psql -U calculator -d calculator_history

# Query calculation history
SELECT * FROM calculation_history ORDER BY timestamp DESC LIMIT 10;
```

### Infrastructure Setup (for local development)

```bash
# Start PostgreSQL
docker run -d \
  --name calculator-postgres \
  -e POSTGRES_DB=calculator_history \
  -e POSTGRES_USER=calculator \
  -e POSTGRES_PASSWORD=calculator123 \
  -p 5432:5432 \
  postgres:15-alpine

# Start Redis
docker run -d \
  --name calculator-redis \
  -p 6379:6379 \
  redis:7-alpine
```

## Project Structure

```
distributed-calculator/
├── api-gateway/                 # Kotlin + Spring Boot
├── services/
│   ├── addition-service/        # Kotlin + Spring Boot  
│   ├── subtraction-service/     # Java + Spring Boot
│   ├── multiplication-service/  # Go + Gin
│   ├── division-service/        # Rust + Actix-web
│   └── history-service/         # Kotlin + Spring Boot + PostgreSQL
├── shared/
│   └── contracts/               # Shared data contracts
├── docker-compose.yml
└── README.md
```

## Features

- **Clean Architecture**: Each service follows hexagonal architecture principles
- **SOLID Principles**: Dependency inversion, single responsibility, etc.
- **Polyglot Programming**: Different languages for each service
- **Resilience Patterns**: Circuit breakers, timeouts, fallbacks via Resilience4j
- **Caching**: Two-tier caching (in-memory L1 + Redis L2)
- **History Tracking**: Automatic calculation history with PostgreSQL persistence
- **Request Tracing**: Request IDs for distributed tracing
- **Observability**: Health checks, metrics, structured logging
- **Containerization**: Docker containers for all services

## Key Implementation Details

- **API Gateway** routes requests to appropriate services based on operation type
- **History Service** automatically receives calculation results from API Gateway
- **Caching Strategy** uses cache keys like `{operation}:{operand1}:{operand2}`
- **Circuit Breakers** protect against service failures with 50% failure threshold
- **Input Validation** limits operands to 10 decimal places and 10 billion max value
- **Error Handling** includes division by zero protection and network timeout handling

## Why This Architecture?

This is intentionally over-engineered to demonstrate:
- Clean Architecture scalability across distributed systems
- SOLID principles in microservices context  
- Polyglot programming challenges and solutions
- Production-ready resilience patterns
- Trade-offs between simplicity and architectural purity

A real calculator would be a single service, obviously. But where's the fun in that?