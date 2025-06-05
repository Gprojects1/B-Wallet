package parser

import (
	"anti_fraud/internal/model"
	"context"
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"time"

	"gorm.io/gorm"
)

type CurrencyParser struct {
	db             *gorm.DB
	apiURL         string
	updateInterval time.Duration
}

func NewCurrencyParser(db *gorm.DB, apiURL string, interval time.Duration) *CurrencyParser {
	return &CurrencyParser{
		db:             db,
		apiURL:         apiURL,
		updateInterval: interval,
	}
}

func (p *CurrencyParser) Start(ctx context.Context) {
	ticker := time.NewTicker(p.updateInterval)
	defer ticker.Stop()

	if err := p.updateRates(); err != nil {
		fmt.Printf("Initial rate update failed: %v\n", err)
	}

	for {
		select {
		case <-ticker.C:
			if err := p.updateRates(); err != nil {
				fmt.Printf("Rate update failed: %v\n", err)
			}
		case <-ctx.Done():
			return
		}
	}
}

func (p *CurrencyParser) updateRates() error {
	resp, err := http.Get(p.apiURL)
	if err != nil {
		return fmt.Errorf("failed to fetch rates: %w", err)
	}
	defer resp.Body.Close()

	body, err := io.ReadAll(resp.Body)
	if err != nil {
		return fmt.Errorf("failed to read response: %w", err)
	}

	var rates map[string]float64
	if err := json.Unmarshal(body, &rates); err != nil {
		return fmt.Errorf("failed to parse rates: %w", err)
	}

	for pair, rate := range rates {
		result := p.db.Where(model.ExchangeRate{CurrencyPair: pair}).
			Assign(model.ExchangeRate{Rate: rate, LastUpdated: time.Now()}).
			FirstOrCreate(&model.ExchangeRate{})
		if result.Error != nil {
			return fmt.Errorf("failed to save rate for %s: %w", pair, result.Error)
		}
	}

	return nil
}
