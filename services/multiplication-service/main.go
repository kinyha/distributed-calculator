package main

import (
	"log"
	"multiplication-service/internal/api"
	"multiplication-service/internal/application"
	"multiplication-service/internal/domain"
	"multiplication-service/internal/infrastructure"

	"github.com/gin-gonic/gin"
)

func main() {
	// Setup dependencies
	calculator := domain.NewSimpleMultiplicationCalculator()
	validator := domain.NewMultiplicationInputValidator()
	cache := infrastructure.NewInMemoryCalculationCache()
	
	useCase := application.NewExecuteMultiplicationUseCase(validator, calculator, cache)
	handler := api.NewMultiplicationHandler(useCase)
	
	// Setup router
	router := gin.Default()
	router.POST("/calculate", handler.Calculate)
	router.GET("/health", handler.Health)
	
	log.Println("Multiplication service starting on port 8083")
	if err := router.Run(":8083"); err != nil {
		log.Fatal("Failed to start server:", err)
	}
}