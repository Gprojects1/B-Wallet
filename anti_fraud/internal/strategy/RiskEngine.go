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
	db        *gorm.DB
	LimConfig config.Limits
	ValConfig config.RiskValues
}

func NewRiskEngine(db *gorm.DB, lcfg config.Limits, vcfg config.RiskValues) *RiskEngineImpl {
	return &RiskEngineImpl{
		db:        db,
		LimConfig: lcfg,
		ValConfig: vcfg,
	}
}

func (e *RiskEngineImpl) CalculateRiskScore(ctx context.Context, req *pb.AntiFraudRequest) (float64, error) {
	amount, err := parseAmount(req.Amount)
	if err != nil {
		return e.ValConfig.DefaultMaxRisk, err
	}

	var sender model.User
	if err := e.db.WithContext(ctx).First(&sender, req.SenderId).Error; err != nil {
		return e.ValConfig.DefaultMaxRisk, err
	}

	if err := e.checkTransactionFrequency(ctx, req.SenderId); err != nil {
		return e.ValConfig.DefaultMaxRisk, err
	}

	maxAmount := e.getMaxAmountForRiskLevel(sender.RiskLevel)
	if amount > maxAmount {
		return e.ValConfig.DefaultMaxRisk, errors.New("transaction amount exceeds limit for user's risk level")
	}

	riskScore := e.ValConfig.DefaultMinRisk
	if amount > maxAmount*e.ValConfig.HigtAmountFactor {
		riskScore += e.ValConfig.HigtAmountRiskFactor
	}
	if sender.RiskLevel == "high" {
		riskScore += e.ValConfig.HigtRiskLevelFactor
	} else if sender.RiskLevel == "medium" {
		riskScore += e.ValConfig.MediumRiskLevelFactor
	}

	return riskScore, nil
}

func (e *RiskEngineImpl) getMaxAmountForRiskLevel(riskLevel string) float64 {
	switch riskLevel {
	case "high":
		return e.LimConfig.HighRiskMaxAmountEUR
	case "medium":
		return e.LimConfig.MediumRiskMaxAmountEUR
	default:
		return e.LimConfig.DefaultMaxAmountEUR
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

	if count >= int64(e.LimConfig.TransactionsPerHour) {
		return errors.New("transaction frequency limit exceeded")
	}

	return nil
}

func parseAmount(amountStr string) (float64, error) {
	var amount float64
	_, err := fmt.Sscanf(amountStr, "%f", &amount)
	return amount, err
}
