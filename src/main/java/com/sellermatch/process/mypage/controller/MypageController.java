package com.sellermatch.process.mypage.controller;

import com.sellermatch.process.common.domain.CommonConstant;
import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.profile.domain.Profile;
import com.sellermatch.process.profile.repository.ProfileRepositoryCustom;
import com.sellermatch.process.project.domain.Project;
import com.sellermatch.process.project.repository.ProjectRepository;
import com.sellermatch.process.project.repository.ProjectRepositoryCustom;
import com.sellermatch.process.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api-v1")
public class MypageController {

    private final ProjectRepository projectRepository;
    private final ProjectService projectService;
    private final ProjectRepositoryCustom projectRepositoryCustom;
    private final ProfileRepositoryCustom profileRepositoryCustom;

    @GetMapping("/myPage/myHome/{projMemId}")
    public CommonDTO selectProject(@PathVariable String projMemId) {
        CommonDTO result = new CommonDTO();
        Profile profile = profileRepositoryCustom.getMyProjectCount(projMemId);
        if (profile != null) {
            result.setContent(profile);
        } else {
            result.setResult("ERROR");
            result.setStatus(CommonConstant.ERROR_998);
            result.setContent(new Profile());
        }
        return result;
    }

    @GetMapping("/myPage/registDelng/{projMemId}")
    public CommonDTO selectRegistDelng(@PathVariable String projMemId, Pageable pageable) {
        CommonDTO result = new CommonDTO();
        Page<Project> project = projectRepositoryCustom.getpRegistedProjectList(projMemId, pageable);
        if (project != null) {
            result.setContent(project);
        } else {
            result.setResult("ERROR");
            result.setStatus(CommonConstant.ERROR_998);
            result.setContent(new Project());
        }
        return result;
    }
}