package com.sellermatch.process.withdraw.repository;

import com.sellermatch.process.withdraw.domain.Withdraw;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawRepository extends JpaRepository<Withdraw, Integer> {
}
