package com.calculator.subtraction.domain;

public interface CalculationCache {
    CalculationResult get(CalculationRequest request);
    void store(CalculationRequest request, CalculationResult result);
}