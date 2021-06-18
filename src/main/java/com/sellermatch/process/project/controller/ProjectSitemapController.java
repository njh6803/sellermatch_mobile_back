package com.sellermatch.process.project.controller;

import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.project.domain.Project;
import com.sellermatch.process.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ProjectSitemapController {

    private final ProjectRepository projectRepository;

    @GetMapping("/project/list")
    public CommonDTO getProject(Pageable pageable) {
        CommonDTO result = new CommonDTO();
        Page<Project> project = projectRepository.findAll(pageable);
        result.setContent(project);
        return result;
    }
}
