package com.sellermatch.process.hashtag.repository;

import com.sellermatch.process.hashtag.domain.Hashtaglist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtaglistRepository extends JpaRepository<Hashtaglist, Integer> {
}
