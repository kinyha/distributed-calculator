package com.calculator.history.application

import com.calculator.history.domain.CalculationHistory
import com.calculator.history.domain.HistoryRepository
import com.calculator.history.domain.OperationType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class HistoryService(
    private val historyRepository: HistoryRepository
) {
    
    fun saveCalculation(history: CalculationHistory): CalculationHistory {
        return historyRepository.save(history)
    }
    
    fun getAllHistory(pageable: Pageable): Page<CalculationHistory> {
        return historyRepository.findAll(pageable)
    }
    
    fun getHistoryByOperation(operation: OperationType, pageable: Pageable): Page<CalculationHistory> {
        return historyRepository.findByOperationOrderByTimestampDesc(operation, pageable)
    }
    
    fun getHistoryByDateRange(start: Instant, end: Instant, pageable: Pageable): Page<CalculationHistory> {
        return historyRepository.findByTimestampBetweenOrderByTimestampDesc(start, end, pageable)
    }
    
    fun getStatsByOperation(operation: OperationType): OperationStats {
        val count = historyRepository.countByOperation(operation)
        val avgProcessingTime = historyRepository.getAverageProcessingTime(operation) ?: 0.0
        
        return OperationStats(
            operation = operation,
            totalCalculations = count,
            averageProcessingTime = avgProcessingTime
        )
    }
}