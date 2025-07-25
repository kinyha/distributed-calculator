package com.calculator.addition.infrastructure

import com.calculator.addition.domain.CalculationCache
import com.calculator.addition.domain.CalculationRequest
import com.calculator.addition.domain.CalculationResult
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.CachePut
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class InMemoryCalculationCache : CalculationCache {
    
    private val cache = ConcurrentHashMap<String, CalculationResult>()
    
    @Cacheable("calculations")
    override fun get(request: CalculationRequest): CalculationResult? {
        val key = generateKey(request)
        return cache[key]
    }
    
    @CachePut("calculations")
    override fun store(request: CalculationRequest, result: CalculationResult) {
        val key = generateKey(request)
        cache[key] = result
    }
    
    private fun generateKey(request: CalculationRequest): String {
        return "add:${request.operand1}:${request.operand2}"
    }
}