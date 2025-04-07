package com.example.wallet.controller;

import com.example.wallet.dto.response.BalanceResponseDTO;
import com.example.wallet.dto.response.TransactionHistoryResponseDTO;
import com.example.wallet.model.entity.Tranche;
import com.example.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wallet")
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/balance")
    public ResponseEntity<BalanceResponseDTO> getMyBalance(@RequestHeader(value = "X-User-Id") String userId,
                                                           @RequestHeader(value = "X-User-Roles") String roles
    ) {
        Long id = Long.parseLong(userId);
        BalanceResponseDTO response = walletService.getBalance(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/transactions/history")
    public ResponseEntity<TransactionHistoryResponseDTO> getTransactionHistory(@RequestHeader(value = "X-User-Id") String userId,
                                                                               @RequestParam("startDate") LocalDate startDate,
                                                                               @RequestParam("endDate") LocalDate endDate
    ) {
        Long id = Long.parseLong(userId);
        TransactionHistoryResponseDTO response = walletService.getTransactionHistory(id,startDate,endDate);
        return response.getTranches().isEmpty() ?
                new ResponseEntity("no transactions found", HttpStatus.NO_CONTENT) :
                ResponseEntity.ok(response);
    }

    @GetMapping("/transaction/{trancheId}")
    public ResponseEntity<Tranche> getTransactionInfo(@RequestHeader(value = "X-User-Id") String userId,
                                                      @PathVariable("trancheId") Long trancheId)
    {
        Long id = Long.parseLong(userId);
        Tranche response = walletService.getTransactionInfo(id,trancheId);
        return ResponseEntity.ok(response);
    }
}
