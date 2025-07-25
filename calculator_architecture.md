# Distributed Calculator - Architecture Document

## Overview

Overcomplicated calculator implementing Clean Architecture principles across distributed microservices. Each mathematical operation runs as separate service in different programming language, demonstrating SOLID principles and resilience patterns.

## System Architecture

```
                    ┌─────────────────┐
                    │   API Gateway   │
                    │   (Kotlin)      │
                    └─────────┬───────┘
                              │
              ┌───────────────┼───────────────┐
              │               │               │
    ┌─────────▼─────────┐ ┌───▼────┐ ┌───────▼────────┐
    │  Addition Service │ │   ...  │ │ History Service │
    │    (Kotlin)       │ │        │ │   (Kotlin)      │
    └───────────────────┘ └────────┘ └────────────────┘
```

## Services

### API Gateway (Kotlin + Spring Boot)
- **Port**: 8080
- **Responsibilities**: Request routing, circuit breaking, response aggregation, API documentation
- **Dependencies**: All operation services, History service

### Operation Services
- **Addition Service** (Kotlin + Spring Boot) - Port 8081
- **Subtraction Service** (Java + Spring Boot) - Port 8082  
- **Multiplication Service** (Go + Gin) - Port 8083
- **Division Service** (Rust + Actix-web) - Port 8084

### History Service (Kotlin + Spring Boot)
- **Port**: 8085
- **Responsibilities**: Store calculation history, audit trail
- **Storage**: PostgreSQL

## Clean Architecture Implementation

Each service follows Clean Architecture with clear layer separation:

### Entities (Domain Layer)
```kotlin
// Shared across all services
data class CalculationRequest(
    val operand1: BigDecimal,
    val operand2: BigDecimal,
    val operation: OperationType
)

data class CalculationResult(
    val result: BigDecimal,
    val operation: OperationType,
    val timestamp: Instant,
    val processingTime: Duration
)
```

### Use Cases (Application Layer)
```kotlin
interface CalculationUseCase {
    suspend fun execute(request: CalculationRequest): CalculationResult
}

class ExecuteAdditionUseCase(
    private val validator: InputValidator,
    private val calculator: AdditionCalculator,
    private val cache: CalculationCache
) : CalculationUseCase {
    override suspend fun execute(request: CalculationRequest): CalculationResult {
        validator.validate(request)
        
        cache.get(request)?.let { return it }
        
        val result = calculator.add(request.operand1, request.operand2)
        val calculationResult = CalculationResult(...)
        
        cache.store(request, calculationResult)
        return calculationResult
    }
}
```

### Interface Adapters
- **Controllers**: REST endpoint handlers
- **Gateways**: HTTP clients for inter-service communication
- **Repositories**: Cache and database access

### Frameworks & Drivers
- HTTP servers (Spring Boot, Gin, Actix-web)
- Caching (Redis/In-memory)
- Databases (PostgreSQL)

## SOLID Principles Application

### Single Responsibility Principle (SRP)
- Each service handles exactly one mathematical operation
- Each use case performs single business function
- Clear separation between calculation logic and infrastructure

### Open/Closed Principle (OCP)
- New operations added as new services without modifying existing ones
- Use case interfaces allow extending functionality without changing core logic
- Plugin architecture for validators and formatters

### Liskov Substitution Principle (LSP)
- All operation services implement identical `CalculationService` contract
- Different cache implementations interchangeable via `CalculationCache` interface
- Language-agnostic HTTP contracts

### Interface Segregation Principle (ISP)
- Minimal service contracts - only operation-specific endpoints
- Separated interfaces for different concerns (validation, caching, calculation)
- No fat interfaces forcing unused dependencies

### Dependency Inversion Principle (DIP)
- Use cases depend on abstractions, not concrete implementations
- Infrastructure concerns injected via interfaces
- Cross-service communication through contracts, not direct dependencies

## Service Contracts

### Operation Service Contract
```http
POST /calculate
Content-Type: application/json

{
    "operand1": "123.45",
    "operand2": "67.89",
    "requestId": "uuid"
}

Response:
{
    "result": "191.34",
    "processingTime": "PT0.005S",
    "cached": false
}
```

### Health Check
```http
GET /health
Response: 200 OK
{
    "status": "UP",
    "cache": "UP",
    "lastCalculation": "2025-07-25T10:30:00Z"
}
```

## Resilience Patterns

### Circuit Breakers (API Gateway)
```kotlin
@Component
class OperationServiceClient {
    
    @CircuitBreaker(name = "addition-service", fallbackMethod = "fallbackCalculation")
    @TimeLimiter(name = "addition-service")
    suspend fun callAdditionService(request: CalculationRequest): CalculationResult {
        // HTTP call implementation
    }
    
    private fun fallbackCalculation(request: CalculationRequest, ex: Exception): CalculationResult {
        return CalculationResult.error("Service temporarily unavailable")
    }
}
```

### Caching Strategy
- **L1 Cache**: In-memory cache per service (recent calculations)
- **L2 Cache**: Redis cluster (shared historical results)
- **TTL**: 1 hour for simple operations, 24 hours for complex ones
- **Cache Keys**: `{operation}:{hash(operand1,operand2)}`

### Timeout Configuration
- Service-to-service calls: 3 seconds
- Database operations: 5 seconds
- Cache operations: 1 second

## Technology Stack

| Service | Language | Framework | Database | Cache |
|---------|----------|-----------|----------|-------|
| API Gateway | Kotlin | Spring Boot | - | Redis |
| Addition | Kotlin | Spring Boot | - | In-memory |
| Subtraction | Java | Spring Boot | - | In-memory |
| Multiplication | Go | Gin | - | In-memory |
| Division | Rust | Actix-web | - | In-memory |
| History | Kotlin | Spring Boot | PostgreSQL | Redis |

## Deployment Architecture

### Docker Compose Setup
```yaml
version: '3.8'
services:
  api-gateway:
    build: ./api-gateway
    ports: ["8080:8080"]
    environment:
      - ADDITION_SERVICE_URL=http://addition-service:8081
      - SUBTRACTION_SERVICE_URL=http://subtraction-service:8082
    
  addition-service:
    build: ./addition-service
    ports: ["8081:8081"]
    
  # ... other services
```

### Service Discovery
Simple static configuration via environment variables (no Consul/Eureka complexity for this experiment).

## Testing Strategy

### Unit Tests
- Domain logic (entities, use cases) - 90%+ coverage
- Mock all infrastructure dependencies
- Test SOLID principles compliance

### Integration Tests  
- Service contracts via TestContainers
- End-to-end calculation flows
- Circuit breaker behavior

### Performance Tests
- Load testing with multiple concurrent calculations
- Cache hit/miss ratios
- Service response time percentiles

## Project Structure

```
distributed-calculator/
├── api-gateway/                 # Kotlin + Spring Boot
├── services/
│   ├── addition-service/        # Kotlin + Spring Boot  
│   ├── subtraction-service/     # Java + Spring Boot
│   ├── multiplication-service/  # Go + Gin
│   ├── division-service/        # Rust + Actix-web
│   └── history-service/         # Kotlin + Spring Boot
├── shared/
│   └── contracts/               # API contracts, DTOs
├── docker-compose.yml
└── README.md
```

## Why This Architecture?

This is intentionally over-engineered to demonstrate:
- Clean Architecture scalability across distributed systems
- SOLID principles in microservices context  
- Polyglot programming challenges and solutions
- Production-ready resilience patterns
- Trade-offs between simplicity and architectural purity

Real calculator would be single service, obviously. But where's the fun in that?