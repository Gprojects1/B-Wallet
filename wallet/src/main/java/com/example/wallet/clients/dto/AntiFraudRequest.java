package com.example.wallet.clients.dto;

import com.example.wallet.model.type.CryptoCurrency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AntiFraudRequest {
    private Long transactionId;
    private Long senderId;
    private Long receiverId;
    private BigDecimal amount;
    private CryptoCurrency currency;
}
