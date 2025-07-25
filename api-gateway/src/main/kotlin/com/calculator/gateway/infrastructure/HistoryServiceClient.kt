package com.calculator.gateway.infrastructure

import com.calculator.contracts.CalculationRequest
import com.calculator.contracts.CalculationResult
import com.calculator.contracts.OperationType
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.time.Instant

@Component
class HistoryServiceClient(
    private val webClient: WebClient,
    @Value("\${history.service.url:http://calculator-history:8085}")
    private val historyServiceUrl: String
) {

    fun saveCalculationHistory(
        request: CalculationRequest,
        result: CalculationResult,
        operation: OperationType,
        serviceName: String
    ): Mono<Void> {
        val historyRequest = mapOf(
            "requestId" to request.requestId,
            "operand1" to request.operand1.toString(),
            "operand2" to request.operand2.toString(),
            "operation" to operation.name,
            "result" to result.result.toString(),
            "processingTime" to result.processingTime.toString(),
            "serviceName" to serviceName,
            "cached" to result.cached,
            "timestamp" to Instant.now().toString()
        )

        println("Sending to history service at: $historyServiceUrl/api/v1/history")
        println("History request: $historyRequest")

        return webClient.post()
            .uri("$historyServiceUrl/api/v1/history")
            .bodyValue(historyRequest)
            .retrieve()
            .bodyToMono(Void::class.java)
            .doOnSuccess { println("History saved successfully") }
            .onErrorResume { error -> 
                println("Error saving history: ${error.message}")
                error.printStackTrace()
                Mono.empty() 
            }
    }
}