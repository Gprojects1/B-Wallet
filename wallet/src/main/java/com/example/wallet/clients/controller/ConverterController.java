package com.example.wallet.clients.controller;

import com.example.wallet.clients.service.ConverterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/convert")
public class ConverterController {

    private final ConverterService converterService;
}
