package com.example.wallet.kafka.events.produce;

import com.example.wallet.model.type.TrancheStatus;
import com.example.wallet.model.type.TrancheType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionEvent {
    Long senderId;
    Long receiverId;
    TrancheType type;
    BigDecimal amount;
    LocalDateTime time;
    TrancheStatus status;
}
