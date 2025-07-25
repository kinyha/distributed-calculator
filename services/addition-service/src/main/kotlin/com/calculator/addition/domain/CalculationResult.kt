package com.calculator.addition.domain

import java.math.BigDecimal
import java.time.Duration
import java.time.Instant

data class CalculationResult(
    val result: BigDecimal,
    val processingTime: Duration,
    val cached: Boolean,
    val timestamp: Instant = Instant.now()
)