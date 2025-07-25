package domain

import (
	"errors"
	"github.com/shopspring/decimal"
)

type InputValidator interface {
	Validate(request *CalculationRequest) error
}

type MultiplicationInputValidator struct{}

func NewMultiplicationInputValidator() *MultiplicationInputValidator {
	return &MultiplicationInputValidator{}
}

func (v *MultiplicationInputValidator) Validate(request *CalculationRequest) error {
	if request.RequestID == "" {
		return errors.New("request ID cannot be blank")
	}
	
	if request.Operand1.Exponent() < -10 || request.Operand2.Exponent() < -10 {
		return errors.New("operands cannot have more than 10 decimal places")
	}
	
	limit := decimal.NewFromFloat(1e10)
	if request.Operand1.Abs().GreaterThan(limit) || request.Operand2.Abs().GreaterThan(limit) {
		return errors.New("operands cannot exceed 10 billion")
	}
	
	return nil
}