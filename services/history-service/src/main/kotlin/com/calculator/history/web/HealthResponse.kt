package com.calculator.history.web

import java.time.Instant

data class HealthResponse(
    val status: String,
    val database: String,
    val lastCheck: Instant
)