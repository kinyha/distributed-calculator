package domain

import "github.com/shopspring/decimal"

type MultiplicationCalculator interface {
	Multiply(operand1, operand2 decimal.Decimal) decimal.Decimal
}

type SimpleMultiplicationCalculator struct{}

func NewSimpleMultiplicationCalculator() *SimpleMultiplicationCalculator {
	return &SimpleMultiplicationCalculator{}
}

func (c *SimpleMultiplicationCalculator) Multiply(operand1, operand2 decimal.Decimal) decimal.Decimal {
	return operand1.Mul(operand2)
}