package com.sellermatch.process.reply.repository;

import com.sellermatch.process.reply.domain.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ReplyRepository extends PagingAndSortingRepository<Reply, Integer> {
    Page<Reply> findAll(Pageable pageable);
}
