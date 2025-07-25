package com.calculator.history.domain

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.Instant

interface HistoryRepository : JpaRepository<CalculationHistory, Long> {
    
    fun findByOperationOrderByTimestampDesc(operation: OperationType, pageable: Pageable): Page<CalculationHistory>
    
    fun findByTimestampBetweenOrderByTimestampDesc(
        start: Instant, 
        end: Instant, 
        pageable: Pageable
    ): Page<CalculationHistory>
    
    @Query("SELECT COUNT(h) FROM CalculationHistory h WHERE h.operation = :operation")
    fun countByOperation(@Param("operation") operation: OperationType): Long
    
    @Query("SELECT AVG(h.processingTimeMs) FROM CalculationHistory h WHERE h.operation = :operation")
    fun getAverageProcessingTime(@Param("operation") operation: OperationType): Double?
}