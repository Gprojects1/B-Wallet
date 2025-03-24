package com.example.wallet.service.util;

import com.example.wallet.dto.QRCodeData;
import com.example.wallet.exception.customException.QRGenerationException;
import com.example.wallet.exception.customException.QRParsingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import lombok.RequiredArgsConstructor;

import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@RequiredArgsConstructor
public class QRGenerator {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String generateQRCodeContent(QRCodeData qrCodeData) {
        try {
            return Base64.getEncoder().encodeToString(objectMapper.writeValueAsBytes(qrCodeData));
        } catch (IOException e) {
            throw new QRGenerationException("Failed to generate QR code content");
        }
    }

    public QRCodeData parseQRCodeContent(String qrCodeContent) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(qrCodeContent);
            return objectMapper.readValue(decodedBytes, QRCodeData.class);
        } catch (IOException e) {
            throw new QRParsingException("Failed to parse QR code content");
        }
    }

    public String generateQRCodeImage(String qrCodeContent) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeContent, BarcodeFormat.QR_CODE, 200, 200);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }
}
