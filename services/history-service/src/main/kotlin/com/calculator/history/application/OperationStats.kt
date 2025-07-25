package com.calculator.history.application

import com.calculator.history.domain.OperationType

data class OperationStats(
    val operation: OperationType,
    val totalCalculations: Long,
    val averageProcessingTime: Double
)