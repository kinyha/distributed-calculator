package com.calculator.addition.domain

import java.math.BigDecimal

interface InputValidator {
    fun validate(request: CalculationRequest)
}

class AdditionInputValidator : InputValidator {
    override fun validate(request: CalculationRequest) {
        if (request.requestId.isBlank()) {
            throw IllegalArgumentException("Request ID cannot be blank")
        }
        
        if (request.operand1.scale() > 10 || request.operand2.scale() > 10) {
            throw IllegalArgumentException("Operands cannot have more than 10 decimal places")
        }
        
        if (request.operand1.abs() > BigDecimal("1E+10") || request.operand2.abs() > BigDecimal("1E+10")) {
            throw IllegalArgumentException("Operands cannot exceed 10 billion")
        }
    }
}