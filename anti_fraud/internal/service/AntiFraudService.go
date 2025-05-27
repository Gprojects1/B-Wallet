package service

import (
	"anti_fraud/internal/repository"
	"anti_fraud/internal/strategy"
	pb "anti_fraud/pkg/grpc"
	"context"
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
		riskEngine: riskEngine,
	}
}

func (s *AntiFraudService) CheckTransaction(ctx context.Context, req *pb.AntiFraudRequest) (*pb.AntiFraudResponse, error) {
	return nil, nil
}
