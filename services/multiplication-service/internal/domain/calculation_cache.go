package domain

type CalculationCache interface {
	Get(request *CalculationRequest) *CalculationResult
	Store(request *CalculationRequest, result *CalculationResult)
}