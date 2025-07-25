package com.calculator.addition

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class AdditionServiceApplication

fun main(args: Array<String>) {
    runApplication<AdditionServiceApplication>(*args)
}