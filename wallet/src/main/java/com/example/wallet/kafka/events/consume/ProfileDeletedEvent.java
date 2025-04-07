package com.example.wallet.kafka.events.consume;

import lombok.Data;

@Data
public class ProfileDeletedEvent {
    private Long userId;
    private String email;
}
