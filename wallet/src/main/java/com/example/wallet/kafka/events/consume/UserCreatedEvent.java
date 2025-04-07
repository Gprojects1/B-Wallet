package com.example.wallet.kafka.events.consume;

import lombok.Builder;
import lombok.Data;

@Data
public class UserCreatedEvent {
    private Long userId;
    private String email;
    private String role;
}
