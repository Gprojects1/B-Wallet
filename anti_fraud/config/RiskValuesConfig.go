package config

type RiskValues struct {
	DefaultMaxRisk        float64
	DefaultMinRisk        float64
	HigtAmountFactor      float64
	HigtAmountRiskFactor  float64
	HigtRiskLevelFactor   float64
	MediumRiskLevelFactor float64
}

func GetDefaultRiskValues() RiskValues {
	return RiskValues{
		DefaultMaxRisk:        1.0,
		DefaultMinRisk:        0.0,
		HigtAmountRiskFactor:  0.3,
		HigtRiskLevelFactor:   0.4,
		MediumRiskLevelFactor: 0.2,
		HigtAmountFactor:      0.8,
	}
}
