package com.calculator.subtraction.domain;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

public interface InputValidator {
    void validate(CalculationRequest request);
}

@Component
class SubtractionInputValidator implements InputValidator {
    @Override
    public void validate(CalculationRequest request) {
        if (request.requestId() == null || request.requestId().isBlank()) {
            throw new IllegalArgumentException("Request ID cannot be blank");
        }
        
        if (request.operand1().scale() > 10 || request.operand2().scale() > 10) {
            throw new IllegalArgumentException("Operands cannot have more than 10 decimal places");
        }
        
        BigDecimal limit = new BigDecimal("1E+10");
        if (request.operand1().abs().compareTo(limit) > 0 || 
            request.operand2().abs().compareTo(limit) > 0) {
            throw new IllegalArgumentException("Operands cannot exceed 10 billion");
        }
    }
}