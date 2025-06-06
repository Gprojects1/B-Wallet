package model

import (
	"time"

	"gorm.io/gorm"
)

type ExchangeRate struct {
	gorm.Model
	BaseCurrency  string    `gorm:"size:10;index:idx_base_quote,unique"`
	QuoteCurrency string    `gorm:"size:10;index:idx_base_quote,unique"`
	Rate          float64   `gorm:"not null"`
	LastUpdated   time.Time `gorm:"not null"`
}
