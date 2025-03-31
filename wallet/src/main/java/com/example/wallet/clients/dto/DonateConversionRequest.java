package com.example.wallet.clients.dto;

import com.example.wallet.model.type.CryptoCurrency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonateConversionRequest {
    private Long userId;
    private BigDecimal sourceAmount;
    private CryptoCurrency sourceCurrency;
}
