package com.example.wallet.kafka.service;

import com.example.wallet.clients.dto.AntiFraudRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaAntiFraudProducerService {

    private final KafkaTemplate<String,Object> kafkaTemplate;

    private final String topicName = "fraud_check.events";

    public void sendFraudCheck(AntiFraudRequest request) {
        kafkaTemplate.send(topicName,"requested",request);
    }
}
