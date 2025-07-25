package com.calculator.subtraction.infrastructure;

import com.calculator.subtraction.domain.CalculationCache;
import com.calculator.subtraction.domain.CalculationRequest;
import com.calculator.subtraction.domain.CalculationResult;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryCalculationCache implements CalculationCache {
    
    private final ConcurrentHashMap<String, CalculationResult> cache = new ConcurrentHashMap<>();
    
    @Override
    @Cacheable("calculations")
    public CalculationResult get(CalculationRequest request) {
        String key = generateKey(request);
        return cache.get(key);
    }
    
    @Override
    @CachePut("calculations")
    public void store(CalculationRequest request, CalculationResult result) {
        String key = generateKey(request);
        cache.put(key, result);
    }
    
    private String generateKey(CalculationRequest request) {
        return "subtract:" + request.operand1() + ":" + request.operand2();
    }
}