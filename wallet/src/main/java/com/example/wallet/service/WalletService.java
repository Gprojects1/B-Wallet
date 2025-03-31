package com.example.wallet.service;

import com.example.wallet.dto.response.BalanceResponseDTO;
import com.example.wallet.dto.response.TransactionHistoryResponseDTO;
import com.example.wallet.exception.customException.service.WalletNotFoundException;
import com.example.wallet.model.entity.Tranche;
import com.example.wallet.model.entity.Wallet;
import com.example.wallet.repository.sql.WalletRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final TrancheService trancheService;


    @Transactional
    public BalanceResponseDTO getBalance(Long userId) {

        Wallet wallet = getWallet(userId);

        return BalanceResponseDTO.builder()
                .balance(wallet.getBalance())
                .build();
    }

    @Transactional
    public TransactionHistoryResponseDTO getTransactionHistory(Long id, LocalDate startDate, LocalDate endDate) {

        List<Tranche> tranches = trancheService.findTranchesByPeriod(startDate,endDate);

        BigDecimal amount = tranches.stream()
                .map(Tranche::getAmount)
                .reduce(BigDecimal.ZERO,BigDecimal::add);

        return TransactionHistoryResponseDTO.builder()
                .tranches(tranches)
                .amount(amount)
                .build();
    }

    public Tranche getTransactionInfo(Long userId, Long trancheId) {
        return trancheService.findTranche(userId, trancheId);
    }

    @Transactional
    public Wallet getWallet(Long userId){
        return walletRepository.findByUserId(userId)
                .orElseThrow(() -> new WalletNotFoundException("wallet not found id: " + userId));
    }

    @Transactional
    public Wallet setWallet(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    @Transactional
    public void deleteWallet(Long userId) {
        walletRepository.deleteByUserId(userId);
    }
}
