package com.calculator.addition.domain

interface CalculationCache {
    fun get(request: CalculationRequest): CalculationResult?
    fun store(request: CalculationRequest, result: CalculationResult)
}