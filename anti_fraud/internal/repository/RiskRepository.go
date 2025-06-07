// repository/config_repository.go
package repository

import (
	"anti_fraud/config"
	"anti_fraud/internal/model"
	"context"

	"gorm.io/gorm"
)

type ConfigRepository interface {
	GetLimitConfig(ctx context.Context) (*model.LimitConfig, error)
	GetRiskConfig(ctx context.Context) (*model.RiskConfig, error)
	CreateDefaultConfigs(ctx context.Context) error
}

type configRepository struct {
	db     *gorm.DB
	config *config.Config
}

func NewConfigRepository(db *gorm.DB, config *config.Config) ConfigRepository {
	return &configRepository{
		db:     db,
		config: config,
	}
}

func (r *configRepository) GetLimitConfig(ctx context.Context) (*model.LimitConfig, error) {
	var cfg model.LimitConfig
	err := r.db.WithContext(ctx).Where("name = ?", "default").First(&cfg).Error
	return &cfg, err
}

func (r *configRepository) GetRiskConfig(ctx context.Context) (*model.RiskConfig, error) {
	var cfg model.RiskConfig
	err := r.db.WithContext(ctx).Where("name = ?", "default").First(&cfg).Error
	return &cfg, err
}

func (r *configRepository) CreateDefaultConfigs(ctx context.Context) error {
	limitConfig := &model.LimitConfig{
		Name:                   r.config.LimitConfig.Name,
		DefaultMaxAmountEUR:    r.config.LimitConfig.DefaultMaxAmountEUR,
		HighRiskMaxAmountEUR:   r.config.LimitConfig.HighRiskMaxAmountEUR,
		MediumRiskMaxAmountEUR: r.config.LimitConfig.MediumRiskMaxAmountEUR,
		TransactionsPerHour:    r.config.LimitConfig.TransactionsPerHour,
	}

	result := r.db.WithContext(ctx).FirstOrCreate(limitConfig, "name = ?", "default")
	if result.Error != nil {
		return result.Error
	}

	riskConfig := &model.RiskConfig{
		Name:                  r.config.RiskConfig.Name,
		DefaultMaxRisk:        r.config.RiskConfig.DefaultMaxRisk,
		DefaultMinRisk:        r.config.RiskConfig.DefaultMinRisk,
		HighAmountFactor:      r.config.RiskConfig.HighAmountFactor,
		HighAmountRiskFactor:  r.config.RiskConfig.HighAmountRiskFactor,
		HighRiskLevelFactor:   r.config.RiskConfig.HighRiskLevelFactor,
		MediumRiskLevelFactor: r.config.RiskConfig.MediumRiskLevelFactor,
		MaxRiskScore:          r.config.RiskConfig.MaxRiskScore,
	}

	result = r.db.WithContext(ctx).FirstOrCreate(riskConfig, "name = ?", "default")
	return result.Error
}
