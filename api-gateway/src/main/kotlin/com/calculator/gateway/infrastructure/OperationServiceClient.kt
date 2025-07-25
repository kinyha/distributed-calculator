package com.calculator.gateway.infrastructure

import com.calculator.contracts.CalculationRequest
import com.calculator.contracts.CalculationResult
import com.calculator.contracts.OperationType
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Component
class OperationServiceClient(
    private val webClient: WebClient.Builder
) {
    
    @Value("\${services.addition.url:http://localhost:8081}")
    private lateinit var additionServiceUrl: String
    
    @Value("\${services.subtraction.url:http://localhost:8082}")
    private lateinit var subtractionServiceUrl: String
    
    @Value("\${services.multiplication.url:http://localhost:8083}")
    private lateinit var multiplicationServiceUrl: String
    
    @Value("\${services.division.url:http://localhost:8084}")
    private lateinit var divisionServiceUrl: String

    suspend fun callOperationService(request: CalculationRequest, operation: OperationType): CalculationResult {
        val serviceUrl = getServiceUrl(operation)
        
        return webClient.build()
            .post()
            .uri("$serviceUrl/calculate")
            .bodyValue(request)
            .retrieve()
            .awaitBody<CalculationResult>()
    }

    private fun getServiceUrl(operation: OperationType): String {
        return when (operation) {
            OperationType.ADDITION -> additionServiceUrl
            OperationType.SUBTRACTION -> subtractionServiceUrl
            OperationType.MULTIPLICATION -> multiplicationServiceUrl
            OperationType.DIVISION -> divisionServiceUrl
        }
    }
}