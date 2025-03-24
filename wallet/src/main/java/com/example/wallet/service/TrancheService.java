package com.example.wallet.service;

import com.example.wallet.repository.sql.TrancheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrancheService {

    private final TrancheRepository trancheRepository;
}
