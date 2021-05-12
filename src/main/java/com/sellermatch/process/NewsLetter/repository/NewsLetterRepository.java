package com.sellermatch.process.NewsLetter.repository;

import com.sellermatch.process.NewsLetter.domain.NewsLetter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface NewsLetterRepository extends PagingAndSortingRepository<NewsLetter, Integer> {
    Page<NewsLetter> findAll(Pageable pageable);
}
