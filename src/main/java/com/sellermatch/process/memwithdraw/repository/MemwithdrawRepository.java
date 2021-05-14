package com.sellermatch.process.memwithdraw.repository;

import com.sellermatch.process.memwithdraw.domain.MemWithdraw;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MemwithdrawRepository extends PagingAndSortingRepository<MemWithdraw, Integer> {

    Page<MemWithdraw> findAll(Pageable pageable);
}

