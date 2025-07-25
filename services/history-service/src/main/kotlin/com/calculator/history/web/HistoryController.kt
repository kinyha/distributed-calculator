package com.calculator.history.web

import com.calculator.history.application.HistoryService
import com.calculator.history.application.OperationStats
import com.calculator.history.domain.CalculationHistory
import com.calculator.history.domain.OperationType
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
@RequestMapping("/api/v1/history")
class HistoryController(
    private val historyService: HistoryService
) {
    
    @PostMapping
    fun saveCalculation(@RequestBody request: SaveCalculationRequest): ResponseEntity<CalculationHistory> {
        val history = CalculationHistory(
            requestId = request.requestId,
            operand1 = request.operand1,
            operand2 = request.operand2,
            operation = request.operation,
            result = request.result,
            processingTimeMs = request.processingTimeMs,
            cached = request.cached,
            timestamp = request.timestamp ?: Instant.now(),
            serviceName = request.serviceName
        )
        
        val saved = historyService.saveCalculation(history)
        return ResponseEntity.ok(saved)
    }
    
    @GetMapping
    fun getAllHistory(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "timestamp") sortBy: String,
        @RequestParam(defaultValue = "desc") sortDir: String
    ): ResponseEntity<Page<CalculationHistory>> {
        val direction = if (sortDir.lowercase() == "desc") Sort.Direction.DESC else Sort.Direction.ASC
        val pageable = PageRequest.of(page, size, Sort.by(direction, sortBy))
        val history = historyService.getAllHistory(pageable)
        return ResponseEntity.ok(history)
    }
    
    @GetMapping("/operation/{operation}")
    fun getHistoryByOperation(
        @PathVariable operation: OperationType,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<CalculationHistory>> {
        val pageable = PageRequest.of(page, size)
        val history = historyService.getHistoryByOperation(operation, pageable)
        return ResponseEntity.ok(history)
    }
    
    @GetMapping("/date-range")
    fun getHistoryByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) start: Instant,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) end: Instant,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<CalculationHistory>> {
        val pageable = PageRequest.of(page, size)
        val history = historyService.getHistoryByDateRange(start, end, pageable)
        return ResponseEntity.ok(history)
    }
    
    @GetMapping("/stats/{operation}")
    fun getStatsByOperation(@PathVariable operation: OperationType): ResponseEntity<OperationStats> {
        val stats = historyService.getStatsByOperation(operation)
        return ResponseEntity.ok(stats)
    }
    
    @GetMapping("/health")
    fun health(): ResponseEntity<HealthResponse> {
        return ResponseEntity.ok(
            HealthResponse(
                status = "UP",
                database = "UP",
                lastCheck = Instant.now()
            )
        )
    }
}