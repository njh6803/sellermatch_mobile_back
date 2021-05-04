package com.sellermatch.process.file.repository;

import com.sellermatch.process.file.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Integer> {
}
