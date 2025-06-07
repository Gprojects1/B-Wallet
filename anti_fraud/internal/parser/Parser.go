package parser

import (
	"context"
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"time"

	"anti_fraud/internal/model"
	"anti_fraud/internal/repository"
)

type CryptoParser interface {
	Start(ctx context.Context)
	UpdateRates(ctx context.Context) error
}

type CoinGeckoCryptoParser struct {
	repo           repository.ExchangeRepository
	apiURL         string
	apiKey         string
	updateInterval time.Duration
}

func NewCryptoParser(
	repo repository.ExchangeRepository,
	apiURL string,
	apiKey string,
	interval time.Duration,
) *CoinGeckoCryptoParser {
	return &CoinGeckoCryptoParser{
		repo:           repo,
		apiURL:         apiURL,
		apiKey:         apiKey,
		updateInterval: interval,
	}
}

func (p *CoinGeckoCryptoParser) Start(ctx context.Context) {
	ticker := time.NewTicker(p.updateInterval)
	defer ticker.Stop()

	if err := p.UpdateRates(ctx); err != nil {
		fmt.Printf("Initial rate update failed: %v\n", err)
	}

	for {
		select {
		case <-ticker.C:
			if err := p.UpdateRates(ctx); err != nil {
				fmt.Printf("Rate update failed: %v\n", err)
			}
		case <-ctx.Done():
			return
		}
	}
}

func (p *CoinGeckoCryptoParser) UpdateRates(ctx context.Context) error {
	rates, err := p.fetchRates(ctx)
	if err != nil {
		return err
	}

	for _, rate := range rates {
		if err := p.repo.UpsertRate(ctx, rate); err != nil {
			return fmt.Errorf("failed to save rate %s/%s: %w",
				rate.BaseCurrency, rate.QuoteCurrency, err)
		}
	}

	return nil
}

func (p *CoinGeckoCryptoParser) fetchRates(ctx context.Context) ([]*model.ExchangeRate, error) {
	req, err := http.NewRequestWithContext(ctx, "GET", p.apiURL, nil)
	if err != nil {
		return nil, fmt.Errorf("failed to create request: %w", err)
	}

	if p.apiKey != "" {
		req.Header.Add("X-CMC_PRO_API_KEY", p.apiKey)
	}

	resp, err := http.DefaultClient.Do(req)
	if err != nil {
		return nil, fmt.Errorf("failed to fetch rates: %w", err)
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return nil, fmt.Errorf("unexpected status code: %d", resp.StatusCode)
	}

	body, err := io.ReadAll(resp.Body)
	if err != nil {
		return nil, fmt.Errorf("failed to read response: %w", err)
	}

	return p.parseRates(body)
}

func (p *CoinGeckoCryptoParser) parseRates(body []byte) ([]*model.ExchangeRate, error) {
	var rates []*model.ExchangeRate

	var cgResponse map[string]map[string]float64
	if err := json.Unmarshal(body, &cgResponse); err == nil {
		for baseCurrency, quotes := range cgResponse {
			for quoteCurrency, price := range quotes {
				rates = append(rates, &model.ExchangeRate{
					BaseCurrency:  baseCurrency,
					QuoteCurrency: quoteCurrency,
					Rate:          price,
					LastUpdated:   time.Now(),
				})
			}
		}
		return rates, nil
	}

	return nil, fmt.Errorf("failed to parse rates from API response")
}
