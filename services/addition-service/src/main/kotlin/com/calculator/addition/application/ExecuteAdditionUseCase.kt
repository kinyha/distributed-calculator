package com.calculator.addition.application

import com.calculator.addition.domain.*
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant

@Service
class ExecuteAdditionUseCase(
    private val validator: InputValidator,
    private val calculator: AdditionCalculator,
    private val cache: CalculationCache
) {
    
    fun execute(request: CalculationRequest): CalculationResult {
        val start = Instant.now()
        
        validator.validate(request)
        
        cache.get(request)?.let { cachedResult ->
            return cachedResult.copy(
                processingTime = Duration.between(start, Instant.now()),
                cached = true
            )
        }
        
        val result = calculator.add(request.operand1, request.operand2)
        val processingTime = Duration.between(start, Instant.now())
        
        val calculationResult = CalculationResult(
            result = result,
            processingTime = processingTime,
            cached = false
        )
        
        cache.store(request, calculationResult)
        return calculationResult
    }
}