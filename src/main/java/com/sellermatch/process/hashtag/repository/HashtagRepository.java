package com.sellermatch.process.hashtag.repository;

import com.sellermatch.process.hashtag.domain.Hashtag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface HashtagRepository extends PagingAndSortingRepository<Hashtag, Integer> {

    Page<Hashtag> findAll(Pageable pageable);

    Optional<Hashtag> findById(String profileId);

}
