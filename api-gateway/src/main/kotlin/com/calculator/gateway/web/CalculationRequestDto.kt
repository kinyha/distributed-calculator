package com.calculator.gateway.web

import java.math.BigDecimal

data class CalculationRequestDto(
    val operand1: BigDecimal,
    val operand2: BigDecimal,
    val requestId: String? = null
)