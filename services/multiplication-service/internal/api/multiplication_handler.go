package api

import (
	"multiplication-service/internal/application"
	"multiplication-service/internal/domain"
	"net/http"
	"time"

	"github.com/gin-gonic/gin"
)

type MultiplicationHandler struct {
	useCase        *application.ExecuteMultiplicationUseCase
	lastCalculation *time.Time
}

type HealthResponse struct {
	Status          string     `json:"status"`
	Cache           string     `json:"cache"`
	LastCalculation *time.Time `json:"lastCalculation,omitempty"`
}

func NewMultiplicationHandler(useCase *application.ExecuteMultiplicationUseCase) *MultiplicationHandler {
	return &MultiplicationHandler{
		useCase: useCase,
	}
}

func (h *MultiplicationHandler) Calculate(c *gin.Context) {
	var request domain.CalculationRequest
	if err := c.ShouldBindJSON(&request); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Invalid request format"})
		return
	}

	result, err := h.useCase.Execute(&request)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	now := time.Now()
	h.lastCalculation = &now
	c.JSON(http.StatusOK, result)
}

func (h *MultiplicationHandler) Health(c *gin.Context) {
	response := HealthResponse{
		Status:          "UP",
		Cache:           "UP",
		LastCalculation: h.lastCalculation,
	}
	c.JSON(http.StatusOK, response)
}