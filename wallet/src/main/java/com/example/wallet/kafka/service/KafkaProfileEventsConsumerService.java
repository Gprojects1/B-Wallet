package com.example.wallet.kafka.service;

import com.example.wallet.kafka.events.consume.ProfileDeletedEvent;
import com.example.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@KafkaListener(topics = "profile.events", groupId = "wallet-service-group")
public class KafkaProfileEventsConsumerService {

    private final WalletService walletService;

    @KafkaHandler
    public void handleUserProfileDeleted(ProfileDeletedEvent event) {
        walletService.deleteWallet(event.getUserId());
    }
}
