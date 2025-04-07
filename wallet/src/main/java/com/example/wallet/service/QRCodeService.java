package com.example.wallet.service;

import com.example.wallet.dto.QRCodeData;
import com.example.wallet.dto.response.QRGeneratedResponseDTO;
import com.example.wallet.exception.customException.system.QRGenerationException;
import com.example.wallet.exception.customException.system.QRValidationException;
import com.example.wallet.repository.redis.QRRedisRepository;
//import com.example.wallet.repository.redis.RedisRepository;
import com.example.wallet.service.util.QRCodeGenerator;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QRCodeService {

    private final QRCodeGenerator qrCodeGenerator;

    private final QRRedisRepository redisRepository;

    private static final long QR_TTL_SEC = 300;

    public QRGeneratedResponseDTO generateQRCode(Long userId, BigDecimal amount) {

        QRCodeData data = new QRCodeData(userId, amount, LocalDate.now());
        String qrCodeId = UUID.randomUUID().toString();

        redisRepository.save(
                qrCodeId,
                data,
                QR_TTL_SEC
        );

        String qrCodeImage;
        try {
            qrCodeImage = qrCodeGenerator.generateQRCodeImage(qrCodeId);
        } catch (WriterException | IOException e) {
            throw new QRGenerationException("QR generation failed!");
        }

        return QRGeneratedResponseDTO.builder()
                .qrCodeId(qrCodeId)
                .qrCodeImage(qrCodeImage)
                .build();
    }

    public QRCodeData validateQRCode(String qrCodeId) {
        QRCodeData data = redisRepository.find(qrCodeId);
        //if(data.getUserId().equals(scannerUserId)) throw new QRValidationException("you cant scan your own qr code!");
        if (data == null) {
            throw new QRValidationException("QR code expired or invalid");
        }
        redisRepository.delete(qrCodeId);
        return data;
    }
}
