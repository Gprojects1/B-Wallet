package com.example.wallet.clients.service;

import com.example.wallet.clients.dto.DonateConversionRequest;
import com.example.wallet.clients.dto.DonateConversionResponse;
import com.example.wallet.clients.dto.WithdrawConversionRequest;
import com.example.wallet.clients.dto.WithdrawConversionResponse;
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


    public DonateConversionResponse processDonateConversion(DonateConversionRequest request, Long userId) {
        return null;
    }

    public WithdrawConversionResponse processWithdrawConversion(WithdrawConversionRequest request, Long userId) {
        return null;
    }
}
