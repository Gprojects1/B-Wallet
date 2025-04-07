package com.example.wallet.kafka.service;

import com.example.wallet.kafka.events.consume.UserCreatedEvent;
import com.example.wallet.model.entity.Wallet;
import com.example.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@KafkaListener(topics = "user.events", groupId = "wallet-service-group")
public class KafkaUserEventsConsumerService {

    private final WalletService walletService;

    @KafkaHandler
    public void handleUserCreatedEvent(UserCreatedEvent event) {

        System.out.println("Received user created event: " + event);

        Wallet wallet = Wallet.builder()
                .userId(event.getUserId())
                .createdAt(LocalDateTime.now())
                .balance(BigDecimal.ZERO)
                .build();
        walletService.setWallet(wallet);
    }
}

