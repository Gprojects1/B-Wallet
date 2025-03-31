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

    public void sendTransactionInitiatedEvent(TransactionInitiatedEvent event) {
        kafkaTemplate.send(topicName,"initiated",event);
    }

    public void sendTransactionFailedEvent(TransactionFailedEvent event) {
        kafkaTemplate.send(topicName,"failed",event);
    }

    public void sendTransactionCompletedEvent(TransactionCompletedEvent event) {
        kafkaTemplate.send(topicName,"completed",event);
    }

}