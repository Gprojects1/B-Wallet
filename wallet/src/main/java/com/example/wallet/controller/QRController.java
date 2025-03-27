package com.example.wallet.controller;

import com.example.wallet.dto.request.QRGenerationRequestDTO;
import com.example.wallet.dto.response.QRGeneratedResponseDTO;
import com.example.wallet.model.entity.Tranche;
import com.example.wallet.service.PaymentService;
import com.example.wallet.service.QRCodeService;
import com.example.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/qr")
public class QRController {

    private final QRCodeService qrCodeService;

    @PostMapping("/generate")
    public ResponseEntity<QRGeneratedResponseDTO> generateQR(@RequestBody QRGenerationRequestDTO qrData,
                                                             @RequestHeader("X-User-Id") String id
    ) {
        Long userId = Long.parseLong(id);
        QRGeneratedResponseDTO qrCodeImage = qrCodeService.generateQRCode(userId,qrData.getAmount(),qrData.getCurrency());
        return ResponseEntity.ok()
                .header("Content-Type", "image/png")
                .body(qrCodeImage);
    }

}
