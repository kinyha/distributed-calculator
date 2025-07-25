package com.calculator.subtraction.web;

import com.calculator.subtraction.application.ExecuteSubtractionUseCase;
import com.calculator.subtraction.domain.CalculationRequest;
import com.calculator.subtraction.domain.CalculationResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
public class SubtractionController {
    
    private final ExecuteSubtractionUseCase executeSubtractionUseCase;
    private Instant lastCalculation;
    
    public SubtractionController(ExecuteSubtractionUseCase executeSubtractionUseCase) {
        this.executeSubtractionUseCase = executeSubtractionUseCase;
    }
    
    @PostMapping("/calculate")
    public ResponseEntity<CalculationResult> calculate(@RequestBody CalculationRequest request) {
        try {
            CalculationResult result = executeSubtractionUseCase.execute(request);
            lastCalculation = Instant.now();
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<HealthResponse> health() {
        return ResponseEntity.ok(new HealthResponse("UP", "UP", lastCalculation));
    }
}