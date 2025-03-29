package com.example.wallet.model.entity;

import com.example.wallet.model.type.Currency;
import com.example.wallet.model.type.TrancheStatus;
import com.example.wallet.model.type.TrancheType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Table(name = "tranches")
public class Tranche {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private Long id;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "receiver_id", nullable = false)
    private Long receiverId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private Currency currency;

    @Column(nullable = false)
    private TrancheStatus status;

    @Column(nullable = false)
    private TrancheType type;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;
}
