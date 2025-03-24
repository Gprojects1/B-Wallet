package com.example.wallet.dto;

import com.example.wallet.model.type.Currency;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class QRCodeData {
    private Long userId;
    private BigDecimal amount;
    private Currency currency;
}
