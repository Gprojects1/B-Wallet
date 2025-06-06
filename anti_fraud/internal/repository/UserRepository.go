package repository

import (
	"anti_fraud/internal/model"
	"context"

	"gorm.io/gorm"
)

type UserRepository interface {
	GetOrCreate(ctx context.Context, userID uint, riskLevel model.RiskLevel) (*model.User, error)
	UpdateRiskLevel(ctx context.Context, userID uint, riskLevel model.RiskLevel) error
}

type userRepository struct {
	db *gorm.DB
}

func NewUserRepository(db *gorm.DB) UserRepository {
	return &userRepository{db: db}
}

func (r *userRepository) GetOrCreate(ctx context.Context, userID uint, riskLevel model.RiskLevel) (*model.User, error) {
	var user model.User
	err := r.db.WithContext(ctx).First(&user, userID).Error

	if err == gorm.ErrRecordNotFound {
		user = model.User{
			ID:        userID,
			RiskLevel: riskLevel,
		}
		if err := r.db.WithContext(ctx).Create(&user).Error; err != nil {
			return nil, err
		}
		return &user, nil
	}

	return &user, err
}

func (r *userRepository) UpdateRiskLevel(ctx context.Context, userID uint, riskLevel model.RiskLevel) error {
	return r.db.WithContext(ctx).Model(&model.User{}).
		Where("id = ?", userID).
		Update("risk_level", riskLevel).Error
}
