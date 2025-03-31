package com.example.wallet.kafka.service;

import com.example.wallet.kafka.events.produce.ConversionCompletedEvent;
import com.example.wallet.kafka.events.produce.ConversionFailedEvent;
import com.example.wallet.kafka.events.produce.ConversionRequestedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConversionProducerService {

    private final KafkaTemplate<String,Object> kafkaTemplate;

    private final String topicName = "conversion.events";

    public void sendConversionRequestedEvent(ConversionRequestedEvent event) {
        kafkaTemplate.send(topicName,"requested",event);
    }

    public void sendConversionFailedEvent(ConversionFailedEvent event) {
        kafkaTemplate.send(topicName,"failed",event);
    }

    public void sendConversionCompletedEvent(ConversionCompletedEvent event) {
        kafkaTemplate.send(topicName,"completed",event);
    }

}
