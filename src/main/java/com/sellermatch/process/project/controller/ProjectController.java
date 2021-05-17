package com.sellermatch.process.project.controller;

import com.sellermatch.process.project.domain.Project;
import com.sellermatch.process.project.domain.ProjectDto;
import com.sellermatch.process.project.repository.ProjectRepository;
import com.sellermatch.process.project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
public class ProjectController {

    @Autowired
    public ProjectRepository projectRepository;

    @Autowired
    public ProjectService projectService;

    @GetMapping("/project")
    public Page<Project> selectProject() {
        Pageable pageable = PageRequest.of(0,1);
        return projectRepository.findAll(pageable);
    }

    @GetMapping("/project/list")
    public Page<Project> selectProjectList(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }

    @PostMapping("/project")
    public Project inseretProject(ProjectDto projectDto,MultipartFile profileImg, MultipartFile projectImg, MultipartFile projectAttFile) throws Exception {
        projectDto.setProfileImgFile(profileImg);
        projectDto.setProjImgFile(projectImg);
        projectDto.setProjAttFile(projectAttFile);
        return projectService.insertAndUpdateProject(projectDto);
    }

    @PutMapping("/project")
    public Project updateProject(ProjectDto projectDto,MultipartFile profileImg, MultipartFile projectImg, MultipartFile projectAttFile) {
        projectRepository.findById(projectDto.getProject().getProjIdx()).ifPresentOrElse(temp -> {
            projectDto.setProfileImgFile(profileImg);
            projectDto.setProjImgFile(projectImg);
            projectDto.setProjAttFile(projectAttFile);
            try {
                projectService.insertAndUpdateProject(projectDto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, () -> {});
        return projectDto.getProject();
    }

    @DeleteMapping("/project")
    public Project deleteProject(Project project) {
        projectRepository.findById(project.getProjIdx()).ifPresentOrElse(temp -> {
            projectRepository.save(project);
        }, () -> {});
        return project;
    }
}
