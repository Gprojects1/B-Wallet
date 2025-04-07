package com.example.wallet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QRCodeData implements Serializable {
    private Long userId;
    private BigDecimal amount;
    private LocalDate createdAt;
}
