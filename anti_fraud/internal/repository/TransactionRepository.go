package repository

import (
	"anti_fraud/internal/model"
	"context"
	"time"

	"gorm.io/gorm"
)

type TransactionRepository interface {
	Create(ctx context.Context, transaction *model.Transaction) error
	GetByID(ctx context.Context, id uint) (*model.Transaction, error)
	CountBySenderSince(ctx context.Context, senderID uint, since time.Time) (int64, error)
	UpdateFraudStatus(ctx context.Context, transactionID uint, isFraud bool) error
}

type transactionRepository struct {
	db *gorm.DB
}

func NewTransactionRepository(db *gorm.DB) TransactionRepository {
	return &transactionRepository{db: db}
}

func (r *transactionRepository) Create(ctx context.Context, transaction *model.Transaction) error {
	return r.db.WithContext(ctx).Create(transaction).Error
}

func (r *transactionRepository) GetByID(ctx context.Context, id uint) (*model.Transaction, error) {
	var transaction model.Transaction
	err := r.db.WithContext(ctx).First(&transaction, id).Error
	return &transaction, err
}

func (r *transactionRepository) CountBySenderSince(ctx context.Context, senderID uint, since time.Time) (int64, error) {
	var count int64
	err := r.db.WithContext(ctx).Model(&model.Transaction{}).
		Where("sender_id = ? AND created_at >= ?", senderID, since).
		Count(&count).Error
	return count, err
}

func (r *transactionRepository) UpdateFraudStatus(ctx context.Context, transactionID uint, isFraud bool) error {
	return r.db.WithContext(ctx).Model(&model.Transaction{}).
		Where("id = ?", transactionID).
		Update("is_fraud", isFraud).Error
}
