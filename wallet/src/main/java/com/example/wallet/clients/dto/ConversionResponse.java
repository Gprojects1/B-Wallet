package com.example.wallet.clients.dto;

import com.example.wallet.clients.enums.ConversionStatus;
import com.example.wallet.clients.enums.ConversionType;
import com.example.wallet.model.type.CryptoCurrency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversionResponse {
    private Long userId;
    private ConversionType conversionType;
    private BigDecimal convertedAmount;
    private Long conversionId;
    private ConversionStatus status;
}
