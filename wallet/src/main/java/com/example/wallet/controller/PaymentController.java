package com.example.wallet.controller;

import com.example.wallet.dto.request.TransferRequestDTO;
import com.example.wallet.dto.response.BalanceResponseDTO;
import com.example.wallet.dto.response.CreateTransferResponseDTO;
import com.example.wallet.model.entity.Tranche;
import com.example.wallet.service.PaymentService;
import com.example.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/transfer")
    public ResponseEntity<CreateTransferResponseDTO> IdTransfer(@RequestHeader(value = "X-User-Id") String userId,
                                                                    @RequestHeader(value = "X-User-Roles") String roles,
                                                                    @RequestBody TransferRequestDTO transferRequest
                                                 ) {
        Long fromId = Long.parseLong(userId);
        CreateTransferResponseDTO response = paymentService.transfer(fromId,transferRequest);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/qr-transfer")
    public ResponseEntity<Tranche> QRTransfer(@RequestHeader("X-User-Id") String id,
                                              @RequestParam String qrCodeId
    ) {
        Long userId = Long.parseLong(id);
        Tranche tranche = paymentService.QRCodeTransfer(userId,qrCodeId);
        return ResponseEntity.ok(tranche);
    }
}
