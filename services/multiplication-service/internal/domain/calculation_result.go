package domain

import (
	"time"
	"github.com/shopspring/decimal"
)

type CalculationResult struct {
	Result         decimal.Decimal `json:"result"`
	ProcessingTime time.Duration   `json:"processingTime"`
	Cached         bool            `json:"cached"`
	Timestamp      time.Time       `json:"timestamp"`
}

func NewCalculationResult(result decimal.Decimal, processingTime time.Duration, cached bool) *CalculationResult {
	return &CalculationResult{
		Result:         result,
		ProcessingTime: processingTime,
		Cached:         cached,
		Timestamp:      time.Now(),
	}
}