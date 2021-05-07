package com.sellermatch.process.project.controller;

import com.sellermatch.process.project.domain.Project;
import com.sellermatch.process.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
public class ProjectController {

    @Autowired
    public ProjectRepository projectRepository;

    @GetMapping("/project")
    public Page<Project> selectProject() {
        Pageable pageable = PageRequest.of(0,1);
        return projectRepository.findAll(pageable);
    }

    @GetMapping("/project")
    public Page<Project> selectProjectList(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }

    @PostMapping("/project")
    public Project inseretProject(Project project) {
        return projectRepository.save(project);
    }

    @PutMapping("/project")
    public Project updateProject(Project project) {
        projectRepository.findById(project.getProjIdx()).ifPresentOrElse(temp -> {
            projectRepository.save(project);
        }, () -> {});
        return project;
    }

    @DeleteMapping("/project")
    public Project deleteProject(Project project) {
        projectRepository.findById(project.getProjIdx()).ifPresentOrElse(temp -> {
            projectRepository.save(project);
        }, () -> {});
        return project;
    }
}
