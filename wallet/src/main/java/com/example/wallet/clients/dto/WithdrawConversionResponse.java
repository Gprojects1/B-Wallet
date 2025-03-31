package com.example.wallet.clients.dto;

import com.example.wallet.model.type.CryptoCurrency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawConversionResponse {
    private BigDecimal convertedAmount;
    private Long conversionId;
    private CryptoCurrency currency;
}
