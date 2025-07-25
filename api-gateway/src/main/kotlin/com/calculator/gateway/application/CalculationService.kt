package com.calculator.gateway.application

import com.calculator.contracts.CalculationRequest
import com.calculator.contracts.CalculationResult
import com.calculator.contracts.OperationType
import com.calculator.gateway.domain.CalculationUseCase
import com.calculator.gateway.infrastructure.OperationServiceClient
import com.calculator.gateway.infrastructure.HistoryServiceClient
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import io.github.resilience4j.timelimiter.annotation.TimeLimiter
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.Duration

@Service
class CalculationService(
    private val operationServiceClient: OperationServiceClient,
    private val historyServiceClient: HistoryServiceClient
) : CalculationUseCase {

    @CircuitBreaker(name = "operation-service", fallbackMethod = "fallbackCalculation")
    @TimeLimiter(name = "operation-service")
    override suspend fun execute(request: CalculationRequest, operation: OperationType): CalculationResult {
        val result = operationServiceClient.callOperationService(request, operation)
        
        // Save to history asynchronously (don't wait for completion)
        val serviceName = getServiceName(operation)
        historyServiceClient.saveCalculationHistory(request, result, operation, serviceName)
            .subscribe()
        
        return result
    }

    private fun getServiceName(operation: OperationType): String {
        return when (operation) {
            OperationType.ADDITION -> "addition-service"
            OperationType.SUBTRACTION -> "subtraction-service"
            OperationType.MULTIPLICATION -> "multiplication-service"
            OperationType.DIVISION -> "division-service"
        }
    }

    private fun fallbackCalculation(request: CalculationRequest, operation: OperationType, ex: Exception): Mono<CalculationResult> {
        return Mono.just(
            CalculationResult(
                result = java.math.BigDecimal.ZERO,
                processingTime = Duration.ZERO,
                cached = false
            )
        )
    }
}