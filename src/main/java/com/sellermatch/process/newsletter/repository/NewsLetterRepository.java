package com.sellermatch.process.newsletter.repository;

import com.sellermatch.process.newsletter.domain.NewsLetter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface NewsLetterRepository extends PagingAndSortingRepository<NewsLetter, Integer> {
    Page<NewsLetter> findAll(Pageable pageable);
}
