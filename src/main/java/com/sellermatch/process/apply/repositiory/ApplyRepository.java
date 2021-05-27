package com.sellermatch.process.apply.repositiory;

import com.sellermatch.process.apply.domain.Apply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface ApplyRepository extends PagingAndSortingRepository<Apply, Integer> {

    Page<Apply> findAll(Pageable pageable);
}
