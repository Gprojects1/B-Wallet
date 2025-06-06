package strategy

import (
	"anti_fraud/internal/dto"
	"anti_fraud/internal/model"
	"errors"
)

type RiskEngine interface {
	CalculateRiskScore(req *dto.RiskCalculationRequest) (float64, error)
}

type RiskEngineImpl struct{}

func NewRiskEngine() *RiskEngineImpl {
	return &RiskEngineImpl{}
}

func (e *RiskEngineImpl) CalculateRiskScore(req *dto.RiskCalculationRequest) (float64, error) {
	if req.LimitConfig == nil || req.RiskConfig == nil {
		return 0, errors.New("configuration is required")
	}

	maxAmount := e.getMaxAmountForRiskLevel(req.UserRiskLevel, req.LimitConfig)
	if req.AmountEUR > maxAmount {
		return req.RiskConfig.DefaultMaxRisk,
			errors.New("transaction amount exceeds limit for user's risk level")
	}

	if req.TransactionCount >= int64(req.LimitConfig.TransactionsPerHour) {
		return req.RiskConfig.DefaultMaxRisk, errors.New("transaction frequency limit exceeded")
	}

	riskScore := req.RiskConfig.DefaultMinRisk
	if req.Amount > maxAmount*req.RiskConfig.HighAmountFactor {
		riskScore += req.RiskConfig.HighAmountRiskFactor
	}
	riskScore += req.UserRiskLevel.Factor(req.RiskConfig)

	return riskScore, nil
}

func (e *RiskEngineImpl) getMaxAmountForRiskLevel(riskLevel model.RiskLevel, config *model.LimitConfig) float64 {
	switch riskLevel {
	case model.RiskHigh:
		return config.HighRiskMaxAmountEUR
	case model.RiskMedium:
		return config.MediumRiskMaxAmountEUR
	default:
		return config.DefaultMaxAmountEUR
	}
}
