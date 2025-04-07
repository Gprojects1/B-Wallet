package com.example.wallet.clients.service;

import com.example.wallet.clients.dto.AntiFraudRequest;
import com.example.wallet.clients.dto.AntiFraudResponse;
import com.example.wallet.clients.exception.customException.FraudCheckFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class AntiFraudService {

    private final RestTemplate restTemplate;

    @Value("${services.anti-fraud.url}")
    private final String antiFraudUrl;

    public AntiFraudResponse checkTransaction(AntiFraudRequest request) {

            ResponseEntity<AntiFraudResponse> response = restTemplate.postForEntity(
                    antiFraudUrl + "anti-fraud/check",
                    request,
                    AntiFraudResponse.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new FraudCheckFailedException("Could not verify fraud status");
            }

            return response.getBody();

    }

}
