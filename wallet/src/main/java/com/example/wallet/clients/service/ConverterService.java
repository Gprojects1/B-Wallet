package com.example.wallet.clients.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ConverterService {

    private final RestTemplate restTemplate;

    @Value("${services.converter.url}")
    private final String converterUrl;
}
