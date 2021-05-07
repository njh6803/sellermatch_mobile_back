package com.sellermatch.process.withdraw.repository;

import com.sellermatch.process.withdraw.domain.Withdraw;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface WithdrawRepository extends PagingAndSortingRepository<Withdraw, Integer> {

    Page<Withdraw> findAll(Pageable pageable);
}
