package com.sellermatch.process.project.repository;

import com.sellermatch.process.project.domain.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ProjectRepository extends PagingAndSortingRepository<Project, Integer> {

    Page<Project> findAll(Pageable pageable);

    Optional<Project> findByProjId(String projId);

    Page<Project> findAllByProjMemIdAndProjStateNot(String projMemId, String projState, Pageable pageable);

    int countByProjMemIdAndProjProdCerti(String projMemId, String ProjProdCerti);

    int countByProjMemIdAndProjProfit(String projMemId, String ProjProfit);
}
