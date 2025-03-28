package com.example.wallet.service;

import com.example.wallet.exception.customException.service.TrancheNotFoundException;
import com.example.wallet.model.entity.Tranche;
import com.example.wallet.repository.sql.TrancheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrancheService {

    private final TrancheRepository trancheRepository;

    public List<Tranche> findTranchesByPeriod(LocalDate start, LocalDate end) {
        return trancheRepository.findByDateBetween(start,end);
    }

    public Tranche findTranche(Long userId, Long trancheId) {
        return trancheRepository.findByUserIdAndTrancheId(userId,trancheId)
                .orElseThrow(() -> new TrancheNotFoundException("tranche not found, id:" + trancheId));
    }
}
