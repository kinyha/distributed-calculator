package com.calculator.history.domain

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.Instant

@Entity
@Table(name = "calculation_history")
data class CalculationHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    
    @Column(name = "request_id", nullable = false, unique = true)
    val requestId: String,
    
    @Column(name = "operand1", nullable = false, precision = 20, scale = 10)
    val operand1: BigDecimal,
    
    @Column(name = "operand2", nullable = false, precision = 20, scale = 10)
    val operand2: BigDecimal,
    
    @Column(name = "operation", nullable = false)
    @Enumerated(EnumType.STRING)
    val operation: OperationType,
    
    @Column(name = "result", nullable = false, precision = 20, scale = 10)
    val result: BigDecimal,
    
    @Column(name = "processing_time_ms", nullable = false)
    val processingTimeMs: Long,
    
    @Column(name = "cached", nullable = false)
    val cached: Boolean,
    
    @Column(name = "timestamp", nullable = false)
    val timestamp: Instant,
    
    @Column(name = "service_name", nullable = false)
    val serviceName: String
)