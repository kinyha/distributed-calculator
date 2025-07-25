package com.calculator.contracts

import java.math.BigDecimal
import java.time.Instant
import java.time.Duration

data class CalculationResult(
    val result: BigDecimal,
    val processingTime: Duration,
    val cached: Boolean,
    val timestamp: Instant = Instant.now()
) {
    companion object {
        fun error(message: String): CalculationResult {
            return CalculationResult(
                result = BigDecimal.ZERO,
                processingTime = Duration.ZERO,
                cached = false,
                timestamp = Instant.now()
            )
        }
    }
}