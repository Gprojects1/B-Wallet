package currency

import (
	"anti_fraud/internal/repository"
	"context"
	"fmt"
	"math"
)

type Converter struct {
	repo repository.ExchangeRepository
}

func NewConverter(repo repository.ExchangeRepository) *Converter {
	return &Converter{repo: repo}
}

func (c *Converter) ConvertToEUR(ctx context.Context, amount float64, currency string) (float64, error) {
	if currency == "EUR" {
		return amount, nil
	}

	rate, err := c.repo.GetRate(ctx, currency, "EUR")
	if err == nil {
		return amount * rate.Rate, nil
	}

	usdRate, err1 := c.repo.GetRate(ctx, currency, "USD")
	eurRate, err2 := c.repo.GetRate(ctx, "USD", "EUR")
	if err1 == nil && err2 == nil {
		return amount * usdRate.Rate * eurRate.Rate, nil
	}

	return 0, fmt.Errorf("failed to convert %s to EUR: %w", currency, err)
}

func (c *Converter) Round(amount float64) float64 {
	return math.Round(amount*100) / 100
}
