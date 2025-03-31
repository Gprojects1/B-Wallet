package com.example.wallet.kafka.events.produce;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ConversionFailedEvent {
    Long userId;
    BigDecimal amount;
    LocalDateTime failedAt;
}
