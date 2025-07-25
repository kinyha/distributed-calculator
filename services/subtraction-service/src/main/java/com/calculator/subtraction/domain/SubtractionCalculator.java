package com.calculator.subtraction.domain;

import java.math.BigDecimal;

public interface SubtractionCalculator {
    BigDecimal subtract(BigDecimal operand1, BigDecimal operand2);
}
