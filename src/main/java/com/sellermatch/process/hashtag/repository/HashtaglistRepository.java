package com.sellermatch.process.hashtag.repository;

import com.sellermatch.process.hashtag.domain.Hashtaglist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface HashtaglistRepository extends PagingAndSortingRepository<Hashtaglist, Integer> {

    Page<Hashtaglist> findAll(Pageable pageable);

    Optional<Hashtaglist> findByHashNm(String hashNm);

    Hashtaglist findByHashId(int hashtagNum);
}
