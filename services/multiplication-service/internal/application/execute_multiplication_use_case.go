package application

import (
	"multiplication-service/internal/domain"
	"time"
)

type ExecuteMultiplicationUseCase struct {
	validator  domain.InputValidator
	calculator domain.MultiplicationCalculator
	cache      domain.CalculationCache
}

func NewExecuteMultiplicationUseCase(
	validator domain.InputValidator,
	calculator domain.MultiplicationCalculator,
	cache domain.CalculationCache,
) *ExecuteMultiplicationUseCase {
	return &ExecuteMultiplicationUseCase{
		validator:  validator,
		calculator: calculator,
		cache:      cache,
	}
}

func (uc *ExecuteMultiplicationUseCase) Execute(request *domain.CalculationRequest) (*domain.CalculationResult, error) {
	start := time.Now()
	
	if err := uc.validator.Validate(request); err != nil {
		return nil, err
	}
	
	if cachedResult := uc.cache.Get(request); cachedResult != nil {
		return domain.NewCalculationResult(
			cachedResult.Result,
			time.Since(start),
			true,
		), nil
	}
	
	result := uc.calculator.Multiply(request.Operand1, request.Operand2)
	processingTime := time.Since(start)
	
	calculationResult := domain.NewCalculationResult(result, processingTime, false)
	uc.cache.Store(request, calculationResult)
	
	return calculationResult, nil
}