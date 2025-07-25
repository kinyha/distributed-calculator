package com.calculator.addition.config

import com.calculator.addition.domain.AdditionCalculator
import com.calculator.addition.domain.InputValidator
import com.calculator.addition.domain.SimpleAdditionCalculator
import com.calculator.addition.domain.AdditionInputValidator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AdditionConfiguration {
    
    @Bean
    fun additionCalculator(): AdditionCalculator {
        return SimpleAdditionCalculator()
    }
    
    @Bean
    fun inputValidator(): InputValidator {
        return AdditionInputValidator()
    }
}