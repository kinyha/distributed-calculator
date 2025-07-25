package com.calculator.subtraction.domain;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;

public record CalculationResult(
    BigDecimal result,
    Duration processingTime,
    boolean cached,
    Instant timestamp
) {
    public CalculationResult(BigDecimal result, Duration processingTime, boolean cached) {
        this(result, processingTime, cached, Instant.now());
    }
}