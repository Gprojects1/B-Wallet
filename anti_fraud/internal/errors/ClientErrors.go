package errors

import "google.golang.org/grpc/codes"

var (
	ErrNilRequest            = NewAntiFraudError("request cannot be nil", codes.InvalidArgument)
	ErrTransactionIDRequired = NewAntiFraudError("transaction_id is required", codes.InvalidArgument)
	ErrUserIDsRequired       = NewAntiFraudError("sender_id and receiver_id are required", codes.InvalidArgument)
	ErrAmountRequired        = NewAntiFraudError("amount is required", codes.InvalidArgument)
	ErrHighRiskUser          = NewAntiFraudError("high risk user", codes.FailedPrecondition)
	ErrUnusualAmount         = NewAntiFraudError("unusual transaction amount", codes.OutOfRange)
)

type AntiFraudError struct {
	message string
	code    codes.Code
}

func NewAntiFraudError(message string, code codes.Code) *AntiFraudError {
	return &AntiFraudError{
		message: message,
		code:    code,
	}
}

func (e *AntiFraudError) Error() string {
	return e.message
}

func (e *AntiFraudError) GRPCCode() codes.Code {
	return e.code
}
