package com.example.wallet.service;

import com.example.wallet.clients.dto.AntiFraudRequest;
import com.example.wallet.clients.service.AntiFraudService;
import com.example.wallet.dto.QRCodeData;
import com.example.wallet.dto.request.TransferRequestDTO;
import com.example.wallet.exception.customException.client.InvalidInteractionException;
import com.example.wallet.kafka.events.produce.TransactionEvent;
import com.example.wallet.kafka.service.KafkaAntiFraudProducerService;
import com.example.wallet.kafka.service.KafkaTransactionProducerService;
import com.example.wallet.model.entity.Tranche;
import com.example.wallet.model.entity.Wallet;
import com.example.wallet.model.type.TrancheStatus;
import com.example.wallet.model.type.TrancheType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final QRCodeService qrCodeService;
    private final WalletService walletService;
    private final TrancheService trancheService;
    private final AntiFraudService antiFraudService;
    private final KafkaTransactionProducerService kafkaTransactionService;
    private final KafkaAntiFraudProducerService kafkaAntiFraudService;

    @Transactional
    public Tranche transfer(Long senderId, TransferRequestDTO transferRequest) {

        if(transferRequest.getReceiverId().equals(senderId)) {
            throw new InvalidInteractionException("you cant transfer yourself");
        }

        Wallet senderWallet = walletService.getWallet(senderId);
        Wallet receiverWallet = walletService.getWallet(transferRequest.getReceiverId());

        if (senderWallet.getBalance().compareTo(transferRequest.getAmount()) < 0) {
            throw new InvalidInteractionException("Insufficient funds, cant create a transaction");
        }

        senderWallet.setBalance(senderWallet.getBalance().subtract(transferRequest.getAmount()));

        receiverWallet.setBalance(receiverWallet.getBalance().add(transferRequest.getAmount()));

        walletService.setWallet(senderWallet);
        walletService.setWallet(receiverWallet);

        Tranche tranche = Tranche.builder()
                .senderId(senderId)
                .receiverId(transferRequest.getReceiverId())
                .amount(transferRequest.getAmount())
                .createdAt(LocalDate.now())
                .status(TrancheStatus.PENDING)
                .type(TrancheType.TRANSFER)
                .build();

        TransactionEvent event = TransactionEvent.builder()
                .time(LocalDateTime.now())
                .amount(tranche.getAmount())
                .receiverId(tranche.getReceiverId())
                .senderId(senderId)
                .type(TrancheType.TRANSFER)
                .status(TrancheStatus.PENDING)
                .build();

        kafkaTransactionService.sendTransactionEvent(event);
        trancheService.saveTranche(tranche);

        AntiFraudRequest antiFraudRequest = AntiFraudRequest.builder()
                .transactionId(tranche.getId())
                .amount(tranche.getAmount())
                .receiverId(tranche.getReceiverId())
                .senderId(tranche.getSenderId())
                .build();

        //AntiFraudResponse response = antiFraudService.checkTransaction(antiFraudRequest);
        kafkaAntiFraudService.sendFraudCheck(antiFraudRequest);

        return tranche;
    }

    @Transactional
    public Tranche QRCodeTransfer(Long senderId, String qrCodeId) {

        QRCodeData qrData = qrCodeService.validateQRCode(qrCodeId);

        if(qrData.getUserId().equals(senderId)) {
            throw new InvalidInteractionException("you cant scan your own QR!");
        }

        Wallet senderWallet = walletService.getWallet(senderId);
        Wallet receiverWallet = walletService.getWallet(qrData.getUserId());

        if (senderWallet.getBalance().compareTo(qrData.getAmount()) < 0) {
            throw new InvalidInteractionException("Insufficient funds, cant create a transaction");
        }

        senderWallet.setBalance(senderWallet.getBalance().subtract(qrData.getAmount()));
        receiverWallet.setBalance(receiverWallet.getBalance().add(qrData.getAmount()));

        walletService.setWallet(senderWallet);
        walletService.setWallet(receiverWallet);

        Tranche tranche = Tranche.builder()
                .senderId(senderId)
                .receiverId(qrData.getUserId())
                .amount(qrData.getAmount())
                .createdAt(LocalDate.now())
                .status(TrancheStatus.PENDING)
                .type(TrancheType.TRANSFER)
                .build();

        TransactionEvent event = TransactionEvent.builder()
                .time(LocalDateTime.now())
                .amount(tranche.getAmount())
                .receiverId(tranche.getReceiverId())
                .senderId(senderId)
                .type(TrancheType.TRANSFER)
                .status(TrancheStatus.PENDING)
                .build();

        kafkaTransactionService.sendTransactionEvent(event);
        trancheService.saveTranche(tranche);

        AntiFraudRequest antiFraudRequest = AntiFraudRequest.builder()
                .transactionId(tranche.getId())
                .amount(tranche.getAmount())
                .receiverId(tranche.getReceiverId())
                .senderId(tranche.getSenderId())
                .build();

        //AntiFraudResponse response = antiFraudService.checkTransaction(antiFraudRequest);
        kafkaAntiFraudService.sendFraudCheck(antiFraudRequest);

        return tranche;
    }

    @Transactional
    public void rollbackTranche(Long trancheId, String reason) {
        // в сервис уведомлений послать и в аудит с причиной
        Tranche tranche = trancheService.getTrancheById(trancheId);
        tranche.setStatus(TrancheStatus.FAILED);

        Wallet senderWallet = walletService.getWallet(tranche.getSenderId());
        Wallet receiverWallet = walletService.getWallet(tranche.getReceiverId());

        senderWallet.setBalance(senderWallet.getBalance().add(tranche.getAmount()));
        receiverWallet.setBalance(receiverWallet.getBalance().subtract(tranche.getAmount()));

        TransactionEvent event = TransactionEvent.builder()
                .time(LocalDateTime.now())
                .amount(tranche.getAmount())
                .receiverId(tranche.getReceiverId())
                .senderId(tranche.getSenderId())
                .type(TrancheType.TRANSFER)
                .status(TrancheStatus.FAILED)
                .build();
        kafkaTransactionService.sendTransactionEvent(event);

        trancheService.saveTranche(tranche);
    }

    @Transactional
    public void commitTranche(Long trancheId) {
        Tranche tranche = trancheService.getTrancheById(trancheId);
        tranche.setStatus(TrancheStatus.COMPLETED);

        TransactionEvent event = TransactionEvent.builder()
                .amount(tranche.getAmount())
                .time(LocalDateTime.now())
                .receiverId(tranche.getReceiverId())
                .senderId(tranche.getSenderId())
                .type(TrancheType.TRANSFER)
                .status(TrancheStatus.COMPLETED)
                .build();
        kafkaTransactionService.sendTransactionEvent(event);

        trancheService.saveTranche(tranche);
    }
}
