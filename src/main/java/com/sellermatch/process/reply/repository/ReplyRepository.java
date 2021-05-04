package com.sellermatch.process.reply.repository;

import com.sellermatch.process.reply.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {
}
