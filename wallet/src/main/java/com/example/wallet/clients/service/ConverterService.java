package com.example.wallet.clients.service;

import com.example.wallet.clients.dto.*;
import com.example.wallet.clients.exception.customException.ConversionFailedException;
import com.example.wallet.clients.exception.customException.FraudCheckFailedException;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.sqm.sql.ConversionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ConverterService {

    private final RestTemplate restTemplate;

    @Value("${services.converter.url}")
    private final String converterUrl;


    public ConversionResponse processConversion(ConversionRequest request, Long userId) {

        request.setUserId(userId);

        ResponseEntity<ConversionResponse> response = restTemplate.postForEntity(
                converterUrl + "/convert",
                request,
                ConversionResponse.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new ConversionFailedException("Conversion error!");
        }

        return response.getBody();
    }
}
