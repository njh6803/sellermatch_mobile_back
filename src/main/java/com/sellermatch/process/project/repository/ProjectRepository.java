package com.sellermatch.process.project.repository;

import com.sellermatch.process.project.domain.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ProjectRepository extends PagingAndSortingRepository<Project, Integer> {

    Page<Project> findAll(Pageable pageable);

    List<Project> findAllByProjMemId(String projMemId);
}
