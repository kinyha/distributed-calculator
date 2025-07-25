package com.calculator.contracts

import java.math.BigDecimal

data class CalculationRequest(
    val operand1: BigDecimal,
    val operand2: BigDecimal,
    val requestId: String
)