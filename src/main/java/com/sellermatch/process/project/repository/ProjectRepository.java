package com.sellermatch.process.project.repository;

import com.sellermatch.process.project.domain.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProjectRepository extends PagingAndSortingRepository<Project, Integer> {

    Page<Project> findAll(Pageable pageable);

    int countByProjMemId(String profile_mem_id);
}
