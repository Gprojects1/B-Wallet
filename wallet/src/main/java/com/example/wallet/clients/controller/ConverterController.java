package com.example.wallet.clients.controller;

import com.example.wallet.clients.dto.ConversionRequest;
import com.example.wallet.clients.dto.ConversionResponse;
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
    public ResponseEntity<ConversionResponse> donate(@RequestBody ConversionRequest request,
                                                     @RequestHeader("X-User-Id") String userId
    ) {
        Long id = Long.parseLong(userId);
        ConversionResponse response = converterService.processConversion(request,id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/out")
    public ResponseEntity<ConversionResponse> withdraw(@RequestBody ConversionRequest request,
                                                       @RequestHeader("X-User-Id") String userId
    ) {
        Long id = Long.parseLong(userId);
        ConversionResponse response = converterService.processConversion(request,id);
        return ResponseEntity.ok(response);
    }
}
