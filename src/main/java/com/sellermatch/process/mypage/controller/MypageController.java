package com.sellermatch.process.mypage.controller;

import com.sellermatch.config.constant.ApplyType;
import com.sellermatch.config.constant.MemberType;
import com.sellermatch.process.apply.domain.Apply;
import com.sellermatch.process.apply.repositiory.ApplyRepository;
import com.sellermatch.process.apply.repositiory.ApplyRepositoryCustom;
import com.sellermatch.process.common.domain.CommonConstant;
import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.profile.domain.Profile;
import com.sellermatch.process.profile.repository.ProfileRepositoryCustom;
import com.sellermatch.process.project.domain.Project;
import com.sellermatch.process.project.repository.ProjectRepository;
import com.sellermatch.process.project.repository.ProjectRepositoryCustom;
import com.sellermatch.process.scrap.repository.ScrapRepository;
import com.sellermatch.util.ControllerResultSet;
import com.sellermatch.util.MailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api-v1")
public class MypageController {

    private final ProjectRepository projectRepository;
    private final ProjectRepositoryCustom projectRepositoryCustom;
    private final ProfileRepositoryCustom profileRepositoryCustom;
    private final ScrapRepository scrapRepository;
    private final ApplyRepository applyRepository;
    private final ApplyRepositoryCustom applyRepositoryCustom;
    private final MailUtil mailUtil;

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
            Profile emptyContent =  new Profile();
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_998, emptyContent);
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
            Project emptyContent =  new Project();
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_998, emptyContent);
        }
        return result;
    }

    @GetMapping("/myPage/recommandList/{memId}/{memSort}")
    public CommonDTO selectRecommandList(@PathVariable String memId, @PathVariable String memSort, Pageable pageable) {
        CommonDTO result = new CommonDTO();
        Page<Project> project = null;
        if (memSort.equalsIgnoreCase(MemberType.PROVIDER.label)){
            project = projectRepositoryCustom.getRecommandListForPro(memId, pageable);
        }
        if (memSort.equalsIgnoreCase(MemberType.SELLER.label)) {
            project = projectRepositoryCustom.getRecommandListForSell(memId, pageable);
        }
        if (project != null) {
            result.setContent(project);
        } else {
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_998);
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
            Project emptyContent = new Project();
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_998, emptyContent);
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
            Project emptyContent = new Project();
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_998, emptyContent);
        }
        return result;
    }

    @GetMapping("/myPage/registDelng/project/{projId}")
    public CommonDTO selectRegistDelngProject(@PathVariable String projId, Pageable pageable) {
        CommonDTO result = new CommonDTO();
        projectRepository.findByProjId(projId).ifPresent(temp -> {

            Project project = projectRepositoryCustom.findProject(temp.getProjIdx(), 0);
            if (project != null) {
                result.setContent(project);
            } else {
                Project emptyContent = new Project();
                ControllerResultSet.errorCode(result, CommonConstant.ERROR_998, emptyContent);
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

    @PutMapping("/myPage/accept")
    public CommonDTO recommand(@RequestBody Apply apply) {
        CommonDTO result = new CommonDTO();
        applyRepository.findById(apply.getApplyIdx()).ifPresentOrElse(temp -> {
            applyRepository.updateApply(apply.getApplyIdx(), apply.getApplyProjState(), apply.getApplyType());
            result.setContent(new Apply());
            String applyTypeName = "";
            String memSortName = "";
            String projTitle = "";
            String subject = "";
            String to = "";
            if (apply.getApplyType().equalsIgnoreCase(ApplyType.APPLY.label)) {
                Apply apply2 = applyRepositoryCustom.getAcceptedProjectOwner(temp);
                projTitle = apply2.getProjTitle();
                applyTypeName = "지원";
                subject = "SellerMatch 거래매칭 승인 결과 발송 메일";
                to = apply2.getMemId();
                if (apply2.getMemSort().equalsIgnoreCase(MemberType.PROVIDER.label)) {
                    memSortName = "판매자";
                }
                if (apply2.getMemSort().equalsIgnoreCase(MemberType.SELLER.label)) {
                    memSortName = "공급자";
                }

            }
            if (apply.getApplyType().equalsIgnoreCase(ApplyType.RECOMMEND.label)) {
                Apply apply2 = applyRepositoryCustom.getAcceptedRecommandOwner(temp);
                projTitle = apply2.getProjTitle();
                applyTypeName = "제안";
                subject = "SellerMatch 거래제안 승인 결과 발송 메일";
                memSortName = "판매자";
                to = apply2.getMemId();
            }

            String type = "aceeot";

            mailUtil.sendMail(to, subject, "", type, applyTypeName, projTitle, memSortName);
       }, () -> {
            Apply emptyContent = new Apply();
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_998, emptyContent);
        });
        return result;
    }
}
