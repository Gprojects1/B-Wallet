package com.example.wallet.kafka.service;

import com.example.wallet.kafka.events.produce.ConversionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConversionProducerService {

    private final KafkaTemplate<String,Object> kafkaTemplate;

    private final String topicName = "conversion.events";

    public void sendConversionEvent(ConversionEvent event) {
        kafkaTemplate.send(topicName,event);
    }
}
