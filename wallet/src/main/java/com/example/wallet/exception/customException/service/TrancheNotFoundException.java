package com.example.wallet.exception.customException.service;

public class TrancheNotFoundException extends RuntimeException {
    public TrancheNotFoundException(String message) {
        super(message);
    }
}
