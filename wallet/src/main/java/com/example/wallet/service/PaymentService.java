package com.example.wallet.service;

import com.example.wallet.dto.QRCodeData;
import com.example.wallet.dto.request.TransferRequestDTO;
import com.example.wallet.dto.response.CreateTransferResponseDTO;
import com.example.wallet.exception.customException.client.InvalidInteraction;
import com.example.wallet.model.entity.Tranche;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final QRCodeService qrCodeService;

    @Transactional
    public CreateTransferResponseDTO transfer(Long senderId, TransferRequestDTO transferRequest) {
        return null;
    }

    @Transactional
    public Tranche QRCodeTransfer(Long senderId, String qrCodeId) {

        QRCodeData qrData = qrCodeService.validateQRCode(qrCodeId);
        if(qrData.getUserId().equals(senderId)) {
            throw new InvalidInteraction("you cant scan your own QR!");
        }

        return null;
    }
}
