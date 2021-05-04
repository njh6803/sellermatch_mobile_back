package com.sellermatch.process.hashtag.repository;

import com.sellermatch.process.hashtag.domain.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagRepository extends JpaRepository<Hashtag, Integer> {
}
