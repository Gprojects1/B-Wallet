package service

import (
	"anti_fraud/internal/model"
	"anti_fraud/internal/repository"
	"anti_fraud/internal/strategy"
	pb "anti_fraud/pkg/grpc"
	"context"
	"fmt"
	"time"
)

type AntiFraudService struct {
	repo       repository.TransactionRepository
	riskEngine strategy.RiskEngine
}

func NewAntiFraudService(
	repo repository.TransactionRepository,
	riskEngine strategy.RiskEngine,
) *AntiFraudService {
	return &AntiFraudService{
		repo:       repo,
		riskEngine: riskEngine,
	}
}

func (s *AntiFraudService) CheckTransaction(ctx context.Context, req *pb.AntiFraudRequest) (*pb.AntiFraudResponse, error) {
	transaction := &model.Transaction{
		SenderID:    uint(req.SenderId),
		ReceiverID:  uint(req.ReceiverId),
		Amount:      parseAmount(req.Amount),
		Timestamp:   time.Now(),
		Description: "Pending fraud check",
		IsFraud:     false,
	}

	if err := s.repo.Create(ctx, transaction); err != nil {
		return nil, fmt.Errorf("failed to create transaction: %w", err)
	}

	riskScore, err := s.riskEngine.CalculateRiskScore(ctx, req)
	if err != nil {
		_ = s.repo.UpdateFraudStatus(ctx, transaction.ID, true)
		return &pb.AntiFraudResponse{
			IsFraud:   true,
			Reason:    fmt.Sprintf("Risk calculation failed: %v", err),
			TrancheId: int64(transaction.ID),
		}, nil
	}

	isFraud := riskScore > 0.7
	reason := ""
	if isFraud {
		reason = fmt.Sprintf("High risk score: %.2f", riskScore)
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

func parseAmount(amountStr string) float64 {
	var amount float64
	fmt.Sscanf(amountStr, "%f", &amount)
	return amount
}
