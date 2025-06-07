package repository

import (
	"anti_fraud/internal/model"
	"context"

	"gorm.io/gorm"
)

type ExchangeRepository interface {
	UpsertRate(ctx context.Context, rate *model.ExchangeRate) error
	GetRate(ctx context.Context, base, quote string) (*model.ExchangeRate, error)
}

type exchangeRepository struct {
	db *gorm.DB
}

func NewExchangeRepository(db *gorm.DB) ExchangeRepository {
	return &exchangeRepository{db: db}
}

func (r *exchangeRepository) UpsertRate(ctx context.Context, rate *model.ExchangeRate) error {
	result := r.db.WithContext(ctx).
		Where(model.ExchangeRate{
			BaseCurrency:  rate.BaseCurrency,
			QuoteCurrency: rate.QuoteCurrency,
		}).
		Assign(rate).
		FirstOrCreate(&model.ExchangeRate{})

	return result.Error
}

func (r *exchangeRepository) GetRate(ctx context.Context, base, quote string) (*model.ExchangeRate, error) {
	var rate model.ExchangeRate
	err := r.db.WithContext(ctx).
		Where("base_currency = ? AND quote_currency = ?", base, quote).
		First(&rate).Error
	return &rate, err
}
