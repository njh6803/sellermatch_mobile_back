package com.sellermatch.process.hashtag.repository;

import com.sellermatch.process.hashtag.domain.Hashtag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface HashtagRepository extends PagingAndSortingRepository<Hashtag, Integer> {

    Page<Hashtag> findAll(Pageable pageable);
}
