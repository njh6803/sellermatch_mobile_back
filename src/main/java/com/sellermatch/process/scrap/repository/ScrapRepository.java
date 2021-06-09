package com.sellermatch.process.scrap.repository;

import com.sellermatch.process.scrap.domain.Scrap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ScrapRepository extends PagingAndSortingRepository<Scrap, Integer> {

    Page<Scrap> findAll(Pageable pageable);

    Page<Scrap> findAllByMemIdx(Pageable pageable, int memIdx);

    Optional<Scrap> findByMemIdxAndProjIdx(Integer memIdx, Integer projIdx);

    int countByMemIdxAndProjIdx(Integer memIdx, Integer projIdx);
}
