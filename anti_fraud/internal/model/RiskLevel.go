package model

type RiskLevel string

const (
	RiskHigh   RiskLevel = "high"
	RiskMedium RiskLevel = "medium"
	RiskLow    RiskLevel = "low"
)

func (r RiskLevel) Factor(config *RiskConfig) float64 {
	switch r {
	case RiskHigh:
		return config.HighRiskLevelFactor
	case RiskMedium:
		return config.MediumRiskLevelFactor
	default:
		return 0
	}
}
