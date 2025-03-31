package com.example.wallet.clients.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AntiFraudService {

    private final RestTemplate restTemplate;

    @Value("${services.anti-fraud.url}")
    private final String antiFraudUrl;
}
