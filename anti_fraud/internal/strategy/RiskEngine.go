package strategy

import (
	"context"
	"errors"
	"time"

	"anti_fraud/config"
	"anti_fraud/internal/model"
	pb "anti_fraud/pkg/grpc"
	"fmt"

	"gorm.io/gorm"
)

type RiskEngine interface {
	CalculateRiskScore(ctx context.Context, req *pb.AntiFraudRequest) (float64, error)
}

type RiskEngineImpl struct {
	db     *gorm.DB
	config config.Limits
}

func NewRiskEngine(db *gorm.DB, cfg config.Limits) *RiskEngineImpl {
	return &RiskEngineImpl{
		db:     db,
		config: cfg,
	}
}

func (e *RiskEngineImpl) CalculateRiskScore(ctx context.Context, req *pb.AntiFraudRequest) (float64, error) {
	amount, err := parseAmount(req.Amount)
	if err != nil {
		return 1.0, err
	}

	var sender model.User
	if err := e.db.WithContext(ctx).First(&sender, req.SenderId).Error; err != nil {
		return 1.0, err
	}

	if err := e.checkTransactionFrequency(ctx, req.SenderId); err != nil {
		return 1.0, err
	}

	maxAmount := e.getMaxAmountForRiskLevel(sender.RiskLevel)
	if amount > maxAmount {
		return 1.0, errors.New("transaction amount exceeds limit for user's risk level")
	}

	riskScore := 0.0
	if amount > maxAmount*0.8 {
		riskScore += 0.3
	}
	if sender.RiskLevel == "high" {
		riskScore += 0.4
	} else if sender.RiskLevel == "medium" {
		riskScore += 0.2
	}

	return riskScore, nil
}

func (e *RiskEngineImpl) getMaxAmountForRiskLevel(riskLevel string) float64 {
	switch riskLevel {
	case "high":
		return e.config.HighRiskMaxAmountEUR
	case "medium":
		return e.config.MediumRiskMaxAmountEUR
	default:
		return e.config.DefaultMaxAmountEUR
	}
}

func (e *RiskEngineImpl) checkTransactionFrequency(ctx context.Context, senderID int64) error {
	var count int64
	oneHourAgo := time.Now().Add(-1 * time.Hour)

	if err := e.db.WithContext(ctx).Model(&model.Transaction{}).
		Where("sender_id = ? AND created_at > ?", senderID, oneHourAgo).
		Count(&count).Error; err != nil {
		return err
	}

	if count >= int64(e.config.TransactionsPerHour) {
		return errors.New("transaction frequency limit exceeded")
	}

	return nil
}

func parseAmount(amountStr string) (float64, error) {
	var amount float64
	_, err := fmt.Sscanf(amountStr, "%f", &amount)
	return amount, err
}
