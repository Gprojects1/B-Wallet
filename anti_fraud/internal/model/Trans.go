package model

import (
	"time"

	"gorm.io/gorm"
)

type Transaction struct {
	ID          uint `gorm:"primaryKey"`
	CreatedAt   time.Time
	UpdatedAt   time.Time
	DeletedAt   gorm.DeletedAt `gorm:"index"`
	SenderID    uint
	Sender      User `gorm:"foreignKey:SenderID"`
	ReceiverID  uint
	Receiver    User      `gorm:"foreignKey:ReceiverID"`
	Amount      float64   `gorm:"not null"`
	Currency    string    `gorm:"size:3"`
	Timestamp   time.Time `gorm:"not null"`
	Description string    `gorm:"size:255"`
	IsFraud     bool      `gorm:"fraud"`
}
