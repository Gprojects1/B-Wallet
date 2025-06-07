// service/anti_fraud_service.go
package service

import (
	"anti_fraud/internal/currency"
	"anti_fraud/internal/dto"
	"anti_fraud/internal/model"
	"anti_fraud/internal/repository"
	"anti_fraud/internal/strategy"
	pb "anti_fraud/pkg/grpc"
	"context"
	"fmt"
	"time"
)

type AntiFraudService struct {
	repo        repository.TransactionRepository
	userRepo    repository.UserRepository
	riskEngine  strategy.RiskEngine
	configCache *repository.ConfigCache
	converter   *currency.Converter
}

func NewAntiFraudService(
	repo repository.TransactionRepository,
	userRepo repository.UserRepository,
	riskEngine strategy.RiskEngine,
	configCache *repository.ConfigCache,
	converter *currency.Converter,
) *AntiFraudService {
	return &AntiFraudService{
		repo:        repo,
		userRepo:    userRepo,
		riskEngine:  riskEngine,
		configCache: configCache,
		converter:   converter,
	}
}

func (s *AntiFraudService) CheckTransaction(ctx context.Context, req *pb.AntiFraudRequest) (*pb.AntiFraudResponse, error) {
	limitConfig, err := s.configCache.GetLimitConfig(ctx)
	if err != nil {
		return nil, fmt.Errorf("failed to get limit config: %w", err)
	}

	riskConfig, err := s.configCache.GetRiskConfig(ctx)
	if err != nil {
		return nil, fmt.Errorf("failed to get risk config: %w", err)
	}

	amount, err := parseAmount(req.Amount)
	if err != nil {
		return nil, fmt.Errorf("invalid amount format: %w", err)
	}

	amountEUR, err := s.converter.ConvertToEUR(ctx, amount, req.Currency)
	if err != nil {
		return nil, fmt.Errorf("currency conversion failed: %w", err)
	}
	amountEUR = s.converter.Round(amountEUR)

	user, err := s.userRepo.GetOrCreate(ctx, uint(req.SenderId), "low")
	if err != nil {
		return nil, fmt.Errorf("failed to get user: %w", err)
	}

	oneHourAgo := time.Now().Add(-1 * time.Hour)
	transactionCount, err := s.repo.CountBySenderSince(ctx, uint(req.SenderId), oneHourAgo)
	if err != nil {
		return nil, fmt.Errorf("failed to count transactions: %w", err)
	}

	transaction := &model.Transaction{
		SenderID:    uint(req.SenderId),
		ReceiverID:  uint(req.ReceiverId),
		Amount:      amount,
		AmountEUR:   amountEUR,
		Currency:    req.Currency,
		Timestamp:   time.Now(),
		Description: "Pending fraud check",
		IsFraud:     false,
	}

	if err := s.repo.Create(ctx, transaction); err != nil {
		return nil, fmt.Errorf("failed to create transaction: %w", err)
	}

	riskReq := &dto.RiskCalculationRequest{
		AmountEUR:        amountEUR,
		OriginalAmount:   amount,
		OriginalCurrency: req.Currency,
		UserRiskLevel:    user.RiskLevel,
		TransactionCount: transactionCount,
		LimitConfig:      limitConfig,
		RiskConfig:       riskConfig,
	}

	riskScore, err := s.riskEngine.CalculateRiskScore(riskReq)
	if err != nil {
		_ = s.repo.UpdateFraudStatus(ctx, transaction.ID, true)
		return &pb.AntiFraudResponse{
			IsFraud:   true,
			Reason:    fmt.Sprintf("Risk calculation failed: %v", err),
			TrancheId: int64(transaction.ID),
		}, nil
	}

	isFraud := riskScore > riskConfig.MaxRiskScore
	reason := ""
	if isFraud {
		reason = fmt.Sprintf("High risk score: %.2f (Amount: %.2f %s ≈ %.2f EUR)",
			riskScore, amount, req.Currency, amountEUR)
	}

	if err := s.repo.UpdateFraudStatus(ctx, transaction.ID, isFraud); err != nil {
		return nil, fmt.Errorf("failed to update transaction status: %w", err)
	}

	return &pb.AntiFraudResponse{
		IsFraud:   isFraud,
		Reason:    reason,
		TrancheId: int64(transaction.ID),
	}, nil
}

func parseAmount(amountStr string) (float64, error) {
	var amount float64
	_, err := fmt.Sscanf(amountStr, "%f", &amount)
	return amount, err
}
