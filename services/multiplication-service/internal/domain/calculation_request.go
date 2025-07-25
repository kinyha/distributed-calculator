package domain

import "github.com/shopspring/decimal"

type CalculationRequest struct {
	Operand1  decimal.Decimal `json:"operand1"`
	Operand2  decimal.Decimal `json:"operand2"`
	RequestID string          `json:"requestId"`
}