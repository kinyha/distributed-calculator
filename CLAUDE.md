# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is an intentionally over-engineered distributed calculator that implements Clean Architecture principles across polyglot microservices. Each mathematical operation runs as a separate service in a different programming language, demonstrating enterprise-grade patterns including SOLID principles, resilience patterns, and multi-level caching.

## Architecture & Service Structure

The system uses hexagonal (Clean Architecture) with clear layer separation across all services:
- **Domain Layer**: Core business logic (`CalculationRequest`, `CalculationResult`, calculators)
- **Application Layer**: Use cases (e.g., `ExecuteAdditionUseCase`, `ExecuteSubtractionUseCase`)
- **Interface Adapters**: Controllers, gateways, repositories, caches
- **Frameworks & Drivers**: HTTP servers, databases, Redis

**Service Communication**: Services communicate via HTTP using shared contracts defined in `/shared/contracts/`. The API Gateway routes requests to appropriate services based on operation type, with circuit breakers and fallbacks implemented via Resilience4j.

**Caching Strategy**: Two-tier caching with in-memory L1 cache per service and Redis L2 cache for shared results. Cache keys follow pattern `{operation}:{operand1}:{operand2}`.

## Common Development Commands

### Full System Operations
```bash
# Start all services with infrastructure
docker-compose up --build

# Background startup
docker-compose up -d --build

# Stop everything
docker-compose down

# View service logs
docker-compose logs -f [api-gateway|addition-service|subtraction-service|multiplication-service|division-service|history-service]
```

### Individual Service Development
```bash
# Kotlin/Java services (Gradle)
cd api-gateway && ./gradlew bootRun
cd services/addition-service && ./gradlew bootRun  
cd services/history-service && ./gradlew bootRun

# Java service (Maven)
cd services/subtraction-service && mvn spring-boot:run

# Go service
cd services/multiplication-service && go run main.go

# Rust service
cd services/division-service && cargo run
```

### Build Commands by Service
```bash
# Gradle builds (Kotlin services)
./gradlew build        # Build JAR
./gradlew test         # Run tests
./gradlew bootRun      # Run locally

# Maven build (Java subtraction service)
mvn clean package     # Build JAR
mvn test              # Run tests
mvn spring-boot:run   # Run locally

# Go build (multiplication service)  
go build              # Build binary
go test ./...         # Run tests
go run main.go        # Run locally

# Rust build (division service)
cargo build           # Build binary
cargo test            # Run tests  
cargo run             # Run locally
```

### Infrastructure Setup
```bash
# PostgreSQL for history service
docker run -d --name calculator-postgres \
  -e POSTGRES_DB=calculator_history \
  -e POSTGRES_USER=calculator \
  -e POSTGRES_PASSWORD=calculator123 \
  -p 5432:5432 postgres:15-alpine

# Redis for caching
docker run -d --name calculator-redis -p 6379:6379 redis:7-alpine
```

## Service Ports & Health Checks
- API Gateway: 8080 (`/actuator/health`)
- Addition: 8081 (`/health`)  
- Subtraction: 8082 (`/health`)
- Multiplication: 8083 (`/health`)
- Division: 8084 (`/health`)
- History: 8085 (`/health`)

## Key Configuration Points

**Circuit Breaker Settings** (API Gateway):
- Sliding window size: 10 requests
- Failure rate threshold: 50%
- Wait duration in open state: 30 seconds
- Timeout duration: 3 seconds per service call

**Database Configuration** (History Service):
- PostgreSQL with JPA/Hibernate
- Database migrations in `src/main/resources/db/migration/`
- Indexes on operation type, timestamp, and request_id

**Service Discovery**: Environment variable-based configuration via Docker Compose networking (e.g., `ADDITION_SERVICE_URL=http://addition-service:8081`).

## Testing the System
```bash
# Test calculation via API Gateway
curl -X POST http://localhost:8080/api/v1/calculator/add \
  -H "Content-Type: application/json" \
  -d '{"operand1": "10.5", "operand2": "5.2"}'

# Check calculation history
curl http://localhost:8085/api/v1/history

# Get operation statistics  
curl http://localhost:8085/api/v1/history/stats/ADDITION
```

## Important Implementation Notes

**Error Handling**: Each service validates inputs (max 10 decimal places, max 10 billion value) and returns appropriate HTTP status codes. Division by zero is handled specifically in the division service.

**Request Tracing**: All requests include a `requestId` for tracking across services. History service stores this for audit trails.

**Cache Keys**: Generated per service using pattern `{operation}:{operand1}:{operand2}` to ensure cache consistency across language implementations.

When modifying services, ensure you maintain the Clean Architecture boundaries and consistent API contracts across all language implementations. The shared contracts in `/shared/contracts/` serve as the source of truth for inter-service communication.