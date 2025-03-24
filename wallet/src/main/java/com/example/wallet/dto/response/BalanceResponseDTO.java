package com.example.wallet.dto.response;

import com.example.wallet.model.type.Currency;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BalanceResponseDTO {
    private BigDecimal balance;
    private Currency currency;
}
