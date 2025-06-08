package controller

import (
	"context"
	"fmt"
	"time"

	converter "github.com/Gprojects1/B-wallet-common-proto/converter/v1"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
)

type ConverterClient struct {
	conn    *grpc.ClientConn
	client  converter.ConverterServiceClient
	timeout time.Duration
}

func NewConverterClient(addr string, timeout time.Duration) (*ConverterClient, error) {
	conn, err := grpc.Dial(addr,
		grpc.WithTransportCredentials(insecure.NewCredentials()),
		grpc.WithBlock(),
	)
	if err != nil {
		return nil, fmt.Errorf("failed to connect to converter service: %w", err)
	}

	return &ConverterClient{
		conn:    conn,
		client:  converter.NewConverterServiceClient(conn),
		timeout: timeout,
	}, nil
}

func (c *ConverterClient) Convert(ctx context.Context, req *converter.ConversionRequest) (*converter.ConversionResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, c.timeout)
	defer cancel()

	return c.client.Convert(ctx, req)
}

func (c *ConverterClient) GetConversionStatus(ctx context.Context, conversionID int64) (*converter.ConversionResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, c.timeout)
	defer cancel()

	req := &converter.ConversionStatusRequest{
		ConversionId: conversionID,
	}
	return c.client.GetConversionStatus(ctx, req)
}

func (c *ConverterClient) Close() error {
	return c.conn.Close()
}
