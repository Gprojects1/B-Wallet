package com.example.wallet.clients.exception.customException;

public class FraudCheckFailedException extends RuntimeException {
    public FraudCheckFailedException(String message) {
        super(message);
    }
}
