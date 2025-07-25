package com.calculator.history.web

import com.calculator.history.domain.OperationType
import java.math.BigDecimal
import java.time.Instant

data class SaveCalculationRequest(
    val requestId: String,
    val operand1: BigDecimal,
    val operand2: BigDecimal,
    val operation: OperationType,
    val result: BigDecimal,
    val processingTimeMs: Long,
    val cached: Boolean,
    val timestamp: Instant? = null,
    val serviceName: String
)