package com.sellermatch.process.project.repository;

import com.sellermatch.process.project.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
}
