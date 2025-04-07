package com.example.wallet.repository.sql;

import com.example.wallet.model.entity.Tranche;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrancheRepository extends JpaRepository<Tranche,Long> {

    List<Tranche> findBySenderId(Long senderId);

    List<Tranche> findByDateBetween(LocalDate startDate, LocalDate enaDate);

    Optional<Tranche> findBySenderIdAndTrancheId(Long userId, Long trancheId);
}
