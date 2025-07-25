package com.calculator.subtraction.web;

import java.time.Instant;

public record HealthResponse(
    String status,
    String cache,
    Instant lastCalculation
) {}