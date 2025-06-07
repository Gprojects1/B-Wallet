package model

type RiskConfig struct {
	Name                  string  `yaml:"name" gorm:"primaryKey"`
	DefaultMaxRisk        float64 `yaml:"default_max_risk"`
	DefaultMinRisk        float64 `yaml:"default_min_risk"`
	HighAmountFactor      float64 `yaml:"high_amount_factor"`
	HighAmountRiskFactor  float64 `yaml:"high_amount_risk_factor"`
	HighRiskLevelFactor   float64 `yaml:"high_risk_level_factor"`
	MediumRiskLevelFactor float64 `yaml:"medium_risk_level_factor"`
	MaxRiskScore          float64 `yaml:"max_risk_score"`
}
