package com.calculator.subtraction.domain;

import java.math.BigDecimal;

public record CalculationRequest(
    BigDecimal operand1,
    BigDecimal operand2,
    String requestId
) {}