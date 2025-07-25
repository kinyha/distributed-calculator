package com.calculator.addition.web

import com.calculator.addition.application.ExecuteAdditionUseCase
import com.calculator.addition.domain.CalculationRequest
import com.calculator.addition.domain.CalculationResult
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
class AdditionController(
    private val executeAdditionUseCase: ExecuteAdditionUseCase
) {
    
    private var lastCalculation: Instant? = null
    
    @PostMapping("/calculate")
    fun calculate(@RequestBody request: CalculationRequest): ResponseEntity<CalculationResult> {
        return try {
            val result = executeAdditionUseCase.execute(request)
            lastCalculation = Instant.now()
            ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }
    
    @GetMapping("/health")
    fun health(): ResponseEntity<HealthResponse> {
        return ResponseEntity.ok(
            HealthResponse(
                status = "UP",
                cache = "UP",
                lastCalculation = lastCalculation
            )
        )
    }
}