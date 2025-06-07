package model

type LimitConfig struct {
	Name                   string  `yaml:"name" gorm:"primaryKey"`
	DefaultMaxAmountEUR    float64 `yaml:"default_max_amount_eur"`
	HighRiskMaxAmountEUR   float64 `yaml:"high_risk_max_amount_eur"`
	MediumRiskMaxAmountEUR float64 `yaml:"medium_risk_max_amount_eur"`
	TransactionsPerHour    uint    `yaml:"transactions_per_hour"`
}
