package com.calculator.subtraction.domain;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SimpleSubtractionCalculator implements SubtractionCalculator {
    @Override
    public BigDecimal subtract(BigDecimal operand1, BigDecimal operand2) {
        return operand1.subtract(operand2);
    }
}
