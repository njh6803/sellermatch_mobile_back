package com.sellermatch.process.file.repository;

import com.sellermatch.process.file.domain.File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface FileRepository extends PagingAndSortingRepository<File, Integer> {

    Page<File> findAll(Pageable pageable);

    /* delete 경우에는 트랜잭션 어노테이션을 꼭 추가해주세요. */
    @Transactional
    @Query(value = "delete from File where fileIdx = :fileIdx")
    void deleteByFileIdx(@Param("fileIdx") int fileIdx);
}
