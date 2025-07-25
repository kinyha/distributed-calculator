package com.calculator.addition.domain

import java.math.BigDecimal

data class CalculationRequest(
    val operand1: BigDecimal,
    val operand2: BigDecimal,
    val requestId: String
)