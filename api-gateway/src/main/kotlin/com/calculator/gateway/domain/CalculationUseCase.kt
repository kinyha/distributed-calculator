package com.calculator.gateway.domain

import com.calculator.contracts.CalculationRequest
import com.calculator.contracts.CalculationResult
import com.calculator.contracts.OperationType

interface CalculationUseCase {
    suspend fun execute(request: CalculationRequest, operation: OperationType): CalculationResult
}