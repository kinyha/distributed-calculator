package com.calculator.addition.domain

import java.math.BigDecimal

interface AdditionCalculator {
    fun add(operand1: BigDecimal, operand2: BigDecimal): BigDecimal
}

class SimpleAdditionCalculator : AdditionCalculator {
    override fun add(operand1: BigDecimal, operand2: BigDecimal): BigDecimal {
        return operand1.add(operand2)
    }
}