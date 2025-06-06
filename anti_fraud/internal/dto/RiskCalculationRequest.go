package dto

import "anti_fraud/internal/model"

type RiskCalculationRequest struct {
	Amount           float64
	UserRiskLevel    model.RiskLevel
	TransactionCount int64
	LimitConfig      *model.LimitConfig
	RiskConfig       *model.RiskConfig
	AmountEUR        float64
	OriginalAmount   float64
	OriginalCurrency string
}
