package com.example.wallet.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class QRGeneratedResponseDTO {
    private String qrCodeId;
    private String qrCodeImage;
}
