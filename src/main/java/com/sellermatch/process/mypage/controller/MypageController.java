package com.sellermatch.process.mypage.controller;

import com.sellermatch.process.common.domain.CommonConstant;
import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.profile.domain.Profile;
import com.sellermatch.process.profile.repository.ProfileRepositoryCustom;
import com.sellermatch.process.project.domain.Project;
import com.sellermatch.process.project.repository.ProjectRepository;
import com.sellermatch.process.project.repository.ProjectRepositoryCustom;
import com.sellermatch.process.scrap.repository.ScrapRepository;
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
    private final ProjectRepositoryCustom projectRepositoryCustom;
    private final ProfileRepositoryCustom profileRepositoryCustom;
    private final ScrapRepository scrapRepository;

    @GetMapping("/myPage/myHome/{projMemId}")
    public CommonDTO selectProject(@PathVariable String projMemId) {
        CommonDTO result = new CommonDTO();
        Profile profile = profileRepositoryCustom.getMyProjectCount(projMemId);
        if (projectRepository.countByProjMemIdAndProjProdCerti(projMemId, "1") > 0) {
            profile.setProjProdCerti("1");
        }
        if (projectRepository.countByProjMemIdAndProjProfit(projMemId, "1") > 0) {
            profile.setProjProfit("1");
        }
        if (profile != null) {
            result.setContent(profile);
        } else {
            result.setResult("ERROR");
            result.setStatus(CommonConstant.ERROR_998);
            result.setContent(new Profile());
        }
        return result;
    }

    @GetMapping("/myPage/registDelng/{memId}")
    public CommonDTO selectRegistDelng(@PathVariable String memId, Pageable pageable) {
        CommonDTO result = new CommonDTO();
        Page<Project> project = projectRepositoryCustom.getpRegistedProjectList(memId, pageable);
        if (project != null) {
            result.setContent(project);
        } else {
            result.setResult("ERROR");
            result.setStatus(CommonConstant.ERROR_998);
            result.setContent(new Project());
        }
        return result;
    }

    @GetMapping("/myPage/recommandList/{memId}/{memSort}")
    public CommonDTO selectRecommandList(@PathVariable String memId, @PathVariable String memSort, Pageable pageable) {
        CommonDTO result = new CommonDTO();
        Page<Project> project;
        if (memSort.equalsIgnoreCase("1")){
            project = projectRepositoryCustom.getRecommandListForPro(memId, pageable);
        } else {
            project = projectRepositoryCustom.getRecommandListForSell(memId, pageable);
        }

        if (project != null) {
            result.setContent(project);
        } else {
            result.setResult("ERROR");
            result.setStatus(CommonConstant.ERROR_998);
        }
        return result;
    }

    @GetMapping("/myPage/myApplyList/{memId}")
    public CommonDTO selectMyApplyList(@PathVariable String memId, Pageable pageable) {
        CommonDTO result = new CommonDTO();
        Page<Project> project = projectRepositoryCustom.getMyApplyList(memId, pageable);

        if (project != null) {
            result.setContent(project);
        } else {
            result.setResult("ERROR");
            result.setStatus(CommonConstant.ERROR_998);
            result.setContent(new Project());
        }
        return result;
    }

    @GetMapping("/myPage/projectEndList/{memId}")
    public CommonDTO selectProjectEndList(@PathVariable String memId, Pageable pageable) {
        CommonDTO result = new CommonDTO();
        Page<Project> project = projectRepositoryCustom.getProjectEndList(memId, pageable);

        if (project != null) {
            result.setContent(project);
        } else {
            result.setResult("ERROR");
            result.setStatus(CommonConstant.ERROR_998);
            result.setContent(new Project());
        }
        return result;
    }

    @GetMapping("/myPage/registDelng/project/{projId}")
    public CommonDTO selectRegistDelngProject(@PathVariable String projId) {
        CommonDTO result = new CommonDTO();
        projectRepository.findByProjId(projId).ifPresent(temp -> {
            Project project = projectRepositoryCustom.findProject(temp.getProjIdx());
            if (project != null) {
                result.setContent(project);
            } else {
                result.setResult("ERROR");
                result.setStatus(CommonConstant.ERROR_998);
                result.setContent(new Profile());
            }
        });
        return result;
    }

    @GetMapping("/myPage/scrap/list/{memIdx}")
    public CommonDTO selectScrapList(@PathVariable Integer memIdx, Pageable pageable) {
        CommonDTO result = new CommonDTO();
        result.setContent(scrapRepository.findAllByMemIdx(pageable, memIdx));
        return result;
    }
}
