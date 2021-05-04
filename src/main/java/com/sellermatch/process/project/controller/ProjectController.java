package com.sellermatch.process.project.controller;

import com.sellermatch.process.project.domain.Project;
import com.sellermatch.process.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProjectController {

    @Autowired
    public ProjectRepository projectRepository;

    @GetMapping("/project")
    public List<Project> selectProject() {
        return projectRepository.findAll();
    }
}
