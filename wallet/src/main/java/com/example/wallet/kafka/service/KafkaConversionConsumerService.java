package com.example.wallet.kafka.service;

import com.example.wallet.clients.dto.AntiFraudResponse;
import com.example.wallet.clients.dto.ConversionResponse;
import com.example.wallet.clients.enums.ConversionStatus;
import com.example.wallet.kafka.events.produce.ConversionEvent;
import com.example.wallet.service.PaymentService;
import com.example.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@KafkaListener(topics = "conversion.events", groupId = "wallet-service-group")
public class KafkaConversionConsumerService {

    private final WalletService walletService;
    private final KafkaConversionProducerService kafkaConversionProducerService;

    @KafkaHandler
    public void handleFraudCheck(ConversionResponse conversion) {
        if (conversion.getStatus().equals(ConversionStatus.ACCEPTED)) {
            walletService.commitConversion(conversion);
        }
        if(conversion.getStatus().equals(ConversionStatus.DECLINED)){
            //послать уведомление что провалено
            return;
        }
    }
}
