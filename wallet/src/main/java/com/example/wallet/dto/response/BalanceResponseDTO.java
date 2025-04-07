package com.example.wallet.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BalanceResponseDTO {
    private BigDecimal balance;
}
