package com.calculator.subtraction.application;

import com.calculator.subtraction.domain.*;
import com.calculator.subtraction.domain.InputValidator;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class ExecuteSubtractionUseCase {
    
    private final InputValidator validator;
    private final com.calculator.subtraction.domain.SubtractionCalculator calculator;
    private final com.calculator.subtraction.domain.CalculationCache cache;
    
    public ExecuteSubtractionUseCase(
            InputValidator validator,
            SubtractionCalculator calculator,
            CalculationCache cache) {
        this.validator = validator;
        this.calculator = calculator;
        this.cache = cache;
    }
    
    public CalculationResult execute(CalculationRequest request) {
        Instant start = Instant.now();
        
        validator.validate(request);
        
        CalculationResult cachedResult = cache.get(request);
        if (cachedResult != null) {
            return new CalculationResult(
                cachedResult.result(),
                Duration.between(start, Instant.now()),
                true
            );
        }
        
        var result = calculator.subtract(request.operand1(), request.operand2());
        var processingTime = Duration.between(start, Instant.now());
        
        var calculationResult = new CalculationResult(result, processingTime, false);
        
        cache.store(request, calculationResult);
        return calculationResult;
    }
}