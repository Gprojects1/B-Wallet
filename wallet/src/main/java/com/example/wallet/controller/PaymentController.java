package com.example.wallet.controller;

import com.example.wallet.dto.request.TransferRequestDTO;
import com.example.wallet.dto.response.BalanceResponseDTO;
import com.example.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wallet")
public class PaymentController {

    private final WalletService walletService;

    @GetMapping("/balance")
    public ResponseEntity<BalanceResponseDTO> getMyBalance(@RequestHeader(value = "X-User-Id") String userId,
                                                           @RequestHeader(value = "X-User-Roles") String roles
    ) {
        Long id = Long.parseLong(userId);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> createTransfer(@RequestHeader(value = "X-User-Id") String userId,
                                                 @RequestHeader(value = "X-User-Roles") String roles,
                                                 @RequestBody TransferRequestDTO transferRequest
                                                 ) {
        Long fromId = Long.parseLong(userId);
        Long toId = transferRequest.getReceiver_id();

        return ResponseEntity.ok(null);
    }

    // GET /wallets/{user_id}/transactions — История транзакций.
    //
    //Вход: limit, offset, type (опционально).
    //
    //Выход: Список транзакций.
    //
    //POST /wallets/{user_id}/generate-qr — Генерация QR-кода.
    //
    //Вход: amount, currency.
    //
    //Выход: qr_code_url.
    //
    //POST /wallets/{user_id}/scan-qr — Обработка QR-кода.
    //
    //Вход: qr_code.
    //
    //Выход: transaction_id.



}
