package strategy

import (
	pb "anti_fraud/pkg/grpc"
	"context"
)

type RiskEngine interface {
	CalculateRiskScore(ctx context.Context, req *pb.AntiFraudRequest) (float64, error)
}
