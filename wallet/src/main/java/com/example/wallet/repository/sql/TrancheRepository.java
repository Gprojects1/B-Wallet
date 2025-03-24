package com.example.wallet.repository.sql;

import com.example.wallet.model.entity.Tranche;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrancheRepository extends JpaRepository<Tranche,Long> {
    List<Tranche> findBySenderId(Long senderId);
}
