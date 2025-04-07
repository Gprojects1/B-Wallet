package com.example.wallet.clients.service;

import com.example.wallet.clients.dto.*;
import com.example.wallet.clients.enums.ConversionStatus;
import com.example.wallet.clients.exception.customException.ConversionFailedException;
import com.example.wallet.clients.exception.customException.FraudCheckFailedException;
import com.example.wallet.kafka.events.produce.ConversionEvent;
import com.example.wallet.kafka.service.KafkaConversionProducerService;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.sqm.sql.ConversionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ConverterService {

    private final RestTemplate restTemplate;

    private final KafkaConversionProducerService kafkaConversionProducerService;

    @Value("${services.converter.url}")
    private final String converterUrl;


    public String processConversion(ConversionRequest request, Long userId) {

        request.setUserId(userId);

        ResponseEntity<String> response = restTemplate.postForEntity(
                converterUrl + "/convert",
                request,
                String.class
        );

        ConversionEvent event = ConversionEvent.builder()
                .amount(request.getSourceAmount())
                .time(LocalDateTime.now())
                .userId(request.getUserId())
                .currency(request.getCurrency())
                .conversionType(request.getConversionType())
                .status(ConversionStatus.REQUESTED)
                .build();
        kafkaConversionProducerService.sendConversionEvent(event);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new ConversionFailedException("Conversion error! Conversion was declined");
        }

        return response.getBody();
    }
}
