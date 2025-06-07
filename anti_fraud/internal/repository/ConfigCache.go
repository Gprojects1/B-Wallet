package repository

import (
	"anti_fraud/internal/model"
	"context"
	"sync"
	"time"
)

type ConfigCache struct {
	mu            sync.RWMutex
	limitConfig   *model.LimitConfig
	riskConfig    *model.RiskConfig
	lastUpdated   time.Time
	refreshPeriod time.Duration
	configRepo    ConfigRepository
}

func NewConfigCache(configRepo ConfigRepository, refreshPeriod time.Duration) *ConfigCache {
	return &ConfigCache{
		configRepo:    configRepo,
		refreshPeriod: refreshPeriod,
	}
}

func (c *ConfigCache) GetLimitConfig(ctx context.Context) (*model.LimitConfig, error) {
	c.mu.RLock()
	if time.Since(c.lastUpdated) < c.refreshPeriod && c.limitConfig != nil {
		config := c.limitConfig
		c.mu.RUnlock()
		return config, nil
	}
	c.mu.RUnlock()

	c.mu.Lock()
	defer c.mu.Unlock()

	if time.Since(c.lastUpdated) < c.refreshPeriod && c.limitConfig != nil {
		return c.limitConfig, nil
	}

	config, err := c.configRepo.GetLimitConfig(ctx)
	if err != nil {
		return nil, err
	}

	c.limitConfig = config
	c.lastUpdated = time.Now()
	return config, nil
}

func (c *ConfigCache) GetRiskConfig(ctx context.Context) (*model.RiskConfig, error) {
	c.mu.RLock()
	if time.Since(c.lastUpdated) < c.refreshPeriod && c.riskConfig != nil {
		config := c.riskConfig
		c.mu.RUnlock()
		return config, nil
	}
	c.mu.RUnlock()

	c.mu.Lock()
	defer c.mu.Unlock()

	if time.Since(c.lastUpdated) < c.refreshPeriod && c.riskConfig != nil {
		return c.riskConfig, nil
	}

	config, err := c.configRepo.GetRiskConfig(ctx)
	if err != nil {
		return nil, err
	}

	c.riskConfig = config
	c.lastUpdated = time.Now()
	return config, nil
}
