package controller

import (
	"context"

	"anti_fraud/internal/errors"
	"anti_fraud/internal/service"
	pb "anti_fraud/pkg/grpc"

	"google.golang.org/grpc/status"
)

type AntiFraudController struct {
	pb.UnimplementedAntiFraudServiceServer
	service *service.AntiFraudService
}

func NewAntiFraudController(svc *service.AntiFraudService) *AntiFraudController {
	return &AntiFraudController{service: svc}
}

func (c *AntiFraudController) CheckTransaction(ctx context.Context, req *pb.AntiFraudRequest) (*pb.AntiFraudResponse, error) {
	if req == nil {
		return nil, status.Error(errors.ErrNilRequest.GRPCCode(), errors.ErrNilRequest.Error())
	}

	if req.TransactionId == 0 {
		return nil, status.Error(errors.ErrTransactionIDRequired.GRPCCode(), errors.ErrTransactionIDRequired.Error())
	}

	if req.SenderId == 0 || req.ReceiverId == 0 {
		return nil, status.Error(errors.ErrUserIDsRequired.GRPCCode(), errors.ErrUserIDsRequired.Error())
	}

	if req.Amount == "" {
		return nil, status.Error(errors.ErrAmountRequired.GRPCCode(), errors.ErrAmountRequired.Error())
	}

	return c.service.CheckTransaction(ctx, req)
}
