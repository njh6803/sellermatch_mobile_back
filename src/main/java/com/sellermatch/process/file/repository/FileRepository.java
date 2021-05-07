package com.sellermatch.process.file.repository;

import com.sellermatch.process.file.domain.File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FileRepository extends PagingAndSortingRepository<File, Integer> {

    Page<File> findAll(Pageable pageable);
}
