package com.example.wallet.kafka.service;

import com.example.wallet.kafka.events.produce.*;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaTransactionProducerService {

    private final KafkaTemplate<String,Object> kafkaTemplate;

    private final String topicName = "transaction.events";

    public void sendTransactionEvent(TransactionEvent event) {
        kafkaTemplate.send(topicName,event);
    }
}