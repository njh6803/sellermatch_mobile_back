package com.sellermatch.process.scrap.repository;

import com.sellermatch.process.scrap.domain.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScrapRepository extends JpaRepository<Scrap, Integer> {
}
