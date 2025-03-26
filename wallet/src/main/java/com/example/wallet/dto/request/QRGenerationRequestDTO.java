package com.example.wallet.dto.request;

import com.example.wallet.model.type.Currency;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class QRGenerationRequestDTO {
    private BigDecimal amount;
    private Currency currency;
}
