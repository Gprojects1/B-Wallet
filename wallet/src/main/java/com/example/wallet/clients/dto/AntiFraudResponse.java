package com.example.wallet.clients.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AntiFraudResponse {
    private boolean isFraud;
    private String reason;
    private Long trancheId;
}
