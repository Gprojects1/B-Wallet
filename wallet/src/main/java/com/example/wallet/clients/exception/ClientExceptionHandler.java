package com.example.wallet.clients.exception;

import com.example.wallet.clients.exception.customException.ConversionFailedException;
import com.example.wallet.clients.exception.customException.FraudCheckFailedException;
import com.example.wallet.exception.customException.client.FraudDetectedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

@RestControllerAdvice
public class ClientExceptionHandler {

    @ExceptionHandler(FraudCheckFailedException.class)
    public ResponseEntity<String> handleCheck(FraudCheckFailedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<String> handleConversion(ConversionFailedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<String> handleClientServerUnavailable(RestClientException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ex.getMessage());
    }

}
