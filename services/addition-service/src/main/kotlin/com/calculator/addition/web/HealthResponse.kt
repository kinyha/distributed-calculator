package com.calculator.addition.web

import java.time.Instant

data class HealthResponse(
    val status: String,
    val cache: String,
    val lastCalculation: Instant?
)