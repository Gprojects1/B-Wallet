package com.example.wallet.exception;

import com.example.wallet.exception.customException.client.InvalidDataException;
import com.example.wallet.exception.customException.client.InvalidInteractionException;
import com.example.wallet.exception.customException.service.TrancheNotFoundException;
import com.example.wallet.exception.customException.service.WalletNotFoundException;
import com.example.wallet.exception.customException.system.QRGenerationException;
import com.example.wallet.exception.customException.system.QRValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WalletServiceExceptionHandler {

    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<String> handleWalletNotFound(WalletNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(TrancheNotFoundException.class)
    public ResponseEntity<String> handleTrancheNotFound(TrancheNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidInteractionException.class)
    public ResponseEntity<String> handleInvalidInteraction(InvalidInteractionException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<String> handleInvalidData(InvalidDataException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(QRGenerationException.class)
    public ResponseEntity<String> handleQRGeneration(QRGenerationException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(QRValidationException.class)
    public ResponseEntity<String> handleQRValidation(QRValidationException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleInternal(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("something went wrong");
    }
}
