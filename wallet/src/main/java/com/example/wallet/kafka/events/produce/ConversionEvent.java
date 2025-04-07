package com.example.wallet.kafka.events.produce;

import com.example.wallet.clients.enums.ConversionStatus;
import com.example.wallet.clients.enums.ConversionType;
import com.example.wallet.model.type.CryptoCurrency;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ConversionEvent {
    Long userId;
    BigDecimal amount;
    LocalDateTime time;
    CryptoCurrency currency;
    ConversionType conversionType;
    ConversionStatus status;
}
