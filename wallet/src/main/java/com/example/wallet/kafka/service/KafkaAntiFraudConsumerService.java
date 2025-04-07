package com.example.wallet.kafka.service;

import com.example.wallet.clients.dto.AntiFraudResponse;
import com.example.wallet.kafka.events.consume.ProfileDeletedEvent;
import com.example.wallet.service.PaymentService;
import com.example.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@KafkaListener(topics = "fraud_check.events", groupId = "wallet-service-group")
public class KafkaAntiFraudConsumerService {

    private final PaymentService paymentService;

    @KafkaHandler
    public void handleFraudCheck(AntiFraudResponse result) {
        if (result.isFraud()) {
            paymentService.rollbackTranche(result.getTrancheId(), result.getReason());
        } else {
            paymentService.commitTranche(result.getTrancheId());
        }
    }
}
