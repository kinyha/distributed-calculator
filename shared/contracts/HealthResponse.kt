package com.calculator.contracts

import java.time.Instant

data class HealthResponse(
    val status: String,
    val cache: String,
    val lastCalculation: Instant?
)