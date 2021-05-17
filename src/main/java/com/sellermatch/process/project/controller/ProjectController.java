package com.sellermatch.process.project.controller;

import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.project.domain.Project;
import com.sellermatch.process.project.domain.ProjectDto;
import com.sellermatch.process.project.repository.ProjectRepository;
import com.sellermatch.process.project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public CommonDTO selectProject() {
        CommonDTO result = new CommonDTO();
        Pageable pageable = PageRequest.of(0,1);
        result.setContent(projectRepository.findAll(pageable));
        return result;
    }

    @GetMapping("/project/list")
    public CommonDTO selectProjectList(Pageable pageable) {
        CommonDTO result = new CommonDTO();
        result.setContent(projectRepository.findAll(pageable));
        return result;
    }

    @PostMapping("/project")
    public CommonDTO inseretProject(ProjectDto projectDto,MultipartFile profileImg, MultipartFile projectImg, MultipartFile projectAttFile) throws Exception {
        CommonDTO result = new CommonDTO();

        projectDto.setProfileImgFile(profileImg);
        projectDto.setProjImgFile(projectImg);
        projectDto.setProjAttFile(projectAttFile);

        result.setContent(projectService.insertAndUpdateProject(projectDto));
        return result;
    }

    @PutMapping("/project")
    public CommonDTO updateProject(ProjectDto projectDto,MultipartFile profileImg, MultipartFile projectImg, MultipartFile projectAttFile) {
        CommonDTO result = new CommonDTO();
        projectRepository.findById(projectDto.getProject().getProjIdx()).ifPresentOrElse(temp -> {
            projectDto.setProfileImgFile(profileImg);
            projectDto.setProjImgFile(projectImg);
            projectDto.setProjAttFile(projectAttFile);
            try {
                result.setContent(projectService.insertAndUpdateProject(projectDto));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, () -> {});
        return result;
    }

    @DeleteMapping("/project")
    public Project deleteProject(Project project) {
        projectRepository.findById(project.getProjIdx()).ifPresentOrElse(temp -> {
            projectRepository.save(project);
        }, () -> {});
        return project;
    }
}
