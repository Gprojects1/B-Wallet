package com.example.wallet.service;

import com.example.wallet.dto.response.BalanceResponseDTO;
import com.example.wallet.dto.response.TransactionHistoryResponseDTO;
import com.example.wallet.model.entity.Tranche;
import com.example.wallet.repository.sql.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final TrancheService trancheService;
    private final QRCodeService qrCodeService;


    public BalanceResponseDTO getBalance(String userId) {
        return null;
    }

    public TransactionHistoryResponseDTO getTransactionHistory(Long id, LocalDate startDate, LocalDate endDate) {
        return null;
    }

    public Tranche getTransactionInfo(String userId, Long trancheId) {
        return null;
    }
}
