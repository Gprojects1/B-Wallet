package com.example.wallet.exception;

import com.example.wallet.exception.customException.QRGenerationException;
import com.example.wallet.exception.customException.QRParsingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WalletServiceExceptionHandler {

    @ExceptionHandler(QRGenerationException.class)
    public ResponseEntity<String> handleQRGeneration(QRGenerationException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(QRParsingException.class)
    public ResponseEntity<String> handleQRParsing(QRParsingException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleInternal(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("something went wrong");
    }
}
