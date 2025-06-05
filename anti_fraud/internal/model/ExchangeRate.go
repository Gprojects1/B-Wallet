package model

import (
	"time"

	"gorm.io/gorm"
)

type ExchangeRate struct {
	gorm.Model
	CurrencyPair string  `gorm:"size:10;uniqueIndex"`
	Rate         float64 `gorm:"not null"`
	LastUpdated  time.Time
}
