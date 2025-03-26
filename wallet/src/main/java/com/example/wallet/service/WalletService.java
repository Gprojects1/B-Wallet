package com.example.wallet.service;

import com.example.wallet.dto.QRCodeData;
import com.example.wallet.exception.customException.client.InvalidInteraction;
import com.example.wallet.model.entity.Tranche;
import com.example.wallet.repository.sql.WalletRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final TrancheService trancheService;
    private final QRCodeService qrCodeService;


    @Transactional
    public Tranche processQRCode(Long senderId, String qrCodeId) {

        QRCodeData qrData = qrCodeService.validateQRCode(qrCodeId);
        if(qrData.getUserId().equals(senderId)) {
            throw new InvalidInteraction("you cant scan your own QR!");
        }

        return null;
    }


}
