package com.calculator.subtraction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SubtractionServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SubtractionServiceApplication.class, args);
    }
}