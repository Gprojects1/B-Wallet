package com.example.wallet.clients.controller;

import com.example.wallet.clients.dto.DonateConversionRequest;
import com.example.wallet.clients.dto.DonateConversionResponse;
import com.example.wallet.clients.dto.WithdrawConversionRequest;
import com.example.wallet.clients.dto.WithdrawConversionResponse;
import com.example.wallet.clients.service.ConverterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/convert")
public class ConverterController {

    private final ConverterService converterService;

    @PostMapping("/in")
    public ResponseEntity<DonateConversionResponse> donate(@RequestBody DonateConversionRequest request,
                                                           @RequestHeader("X-User-Id") String userId
    ) {
        Long id = Long.parseLong(userId);
        DonateConversionResponse response = converterService.processDonateConversion(request,id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/out")
    public ResponseEntity<WithdrawConversionResponse> withdraw(@RequestBody WithdrawConversionRequest request,
                                                               @RequestHeader("X-User-Id") String userId
    ) {
        Long id = Long.parseLong(userId);
        WithdrawConversionResponse response = converterService.processWithdrawConversion(request,id);
        return ResponseEntity.ok(response);
    }
}
