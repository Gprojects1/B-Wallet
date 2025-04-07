package com.example.wallet.dto.response;

import com.example.wallet.model.entity.Tranche;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class TransactionHistoryResponseDTO {
    private List<Tranche> tranches;
    private BigDecimal amount;
}
