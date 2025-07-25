package com.calculator.gateway.web

import com.calculator.contracts.CalculationRequest
import com.calculator.contracts.CalculationResult
import com.calculator.contracts.OperationType
import com.calculator.gateway.application.CalculationService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/calculator")
class CalculatorController(
    private val calculationService: CalculationService
) {

    @PostMapping("/add")
    suspend fun add(@RequestBody request: CalculationRequestDto): CalculationResult {
        val calculationRequest = CalculationRequest(
            operand1 = request.operand1,
            operand2 = request.operand2,
            requestId = request.requestId ?: UUID.randomUUID().toString()
        )
        return calculationService.execute(calculationRequest, OperationType.ADDITION)
    }

    @PostMapping("/subtract")
    suspend fun subtract(@RequestBody request: CalculationRequestDto): CalculationResult {
        val calculationRequest = CalculationRequest(
            operand1 = request.operand1,
            operand2 = request.operand2,
            requestId = request.requestId ?: UUID.randomUUID().toString()
        )
        return calculationService.execute(calculationRequest, OperationType.SUBTRACTION)
    }

    @PostMapping("/multiply")
    suspend fun multiply(@RequestBody request: CalculationRequestDto): CalculationResult {
        val calculationRequest = CalculationRequest(
            operand1 = request.operand1,
            operand2 = request.operand2,
            requestId = request.requestId ?: UUID.randomUUID().toString()
        )
        return calculationService.execute(calculationRequest, OperationType.MULTIPLICATION)
    }

    @PostMapping("/divide")
    suspend fun divide(@RequestBody request: CalculationRequestDto): CalculationResult {
        val calculationRequest = CalculationRequest(
            operand1 = request.operand1,
            operand2 = request.operand2,
            requestId = request.requestId ?: UUID.randomUUID().toString()
        )
        return calculationService.execute(calculationRequest, OperationType.DIVISION)
    }
}