package config

type Limits struct {
	DefaultMaxAmountEUR    float64
	HighRiskMaxAmountEUR   float64
	MediumRiskMaxAmountEUR float64
	TransactionsPerHour    uint
}

func GetDefaultLimits() Limits {
	return Limits{
		DefaultMaxAmountEUR:    100.0,
		HighRiskMaxAmountEUR:   50.0,
		MediumRiskMaxAmountEUR: 75.0,
		TransactionsPerHour:    10,
	}
}
