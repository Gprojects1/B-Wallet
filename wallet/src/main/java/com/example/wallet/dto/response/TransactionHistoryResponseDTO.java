package com.example.wallet.dto.response;

import com.example.wallet.model.entity.Tranche;

import java.math.BigDecimal;
import java.util.List;

public class TransactionHistoryResponseDTO {
    private List<Tranche> tranches;
    private BigDecimal amount;
}
