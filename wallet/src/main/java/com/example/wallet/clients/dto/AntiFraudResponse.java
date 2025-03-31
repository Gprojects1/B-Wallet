package com.example.wallet.clients.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AntiFraudResponse {
    private boolean isFraud;
    private String reason;
}
