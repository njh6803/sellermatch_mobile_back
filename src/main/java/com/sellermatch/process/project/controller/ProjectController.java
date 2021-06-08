package com.sellermatch.process.project.controller;

import com.sellermatch.process.common.domain.CommonConstant;
import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.hashtag.domain.Hashtag;
import com.sellermatch.process.member.repository.MemberRepository;
import com.sellermatch.process.profile.domain.Profile;
import com.sellermatch.process.project.domain.Project;
import com.sellermatch.process.project.domain.ProjectDto;
import com.sellermatch.process.project.repository.ProjectRepository;
import com.sellermatch.process.project.repository.ProjectRepositoryCustom;
import com.sellermatch.process.project.service.ProjectService;
import com.sellermatch.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Date;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api-v1")
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final ProjectService projectService;
    private final ProjectRepositoryCustom projectRepositoryCustom;
    private final MemberRepository memberRepository;

    @GetMapping("/project/{id}")
    public CommonDTO selectProject(@PathVariable Integer id) {
        CommonDTO result = new CommonDTO();
        Project project = projectRepositoryCustom.findProject(id);
        if (project != null) {
            result.setContent(project);
        } else {
            result.setResult("ERROR");
            result.setStatus(CommonConstant.ERROR_998);
            result.setContent(new Profile());
        }
        return result;
    }

    @GetMapping("/project/list/{profileMemId}")
    public CommonDTO getProject(@PathVariable String profileMemId, Pageable pageable) {
        CommonDTO result = new CommonDTO();
        // 판매이력리스트(등록한 거래) - 추후에 페이징처리가 필요할 수 있음
        Page<Project> project = projectRepository.findAllByProjMemId(profileMemId, pageable);
        result.setContent(project);
        return result;
    }

    @GetMapping("/project/list/recommend/{memIdx}")
    public CommonDTO selectRecommendProject(@PathVariable Integer memIdx, Pageable pageable) {
        CommonDTO result = new CommonDTO();
        Project project = new Project();

        //프로젝트 제안하기 마감거래는 조회되지 않도록 하기위한 플래그 "Y"면 마감거래 조회 안하기
        String recommandProjectFlag = "Y";
        memberRepository.findById(memIdx).ifPresentOrElse(temp -> {
            project.setProjMemId(temp.getMemId());
            project.setRecommandProjectFlag(recommandProjectFlag);
            Page<Project> projectList = projectRepositoryCustom.findAllProject(project, pageable, null);
            result.setContent(projectList);

            if (!Util.isEmpty(projectList)) {
                result.setResult("ERROR");
                result.setStatus(CommonConstant.ERROR_NULL_216);
            }
        }, ()->{});
        return result;
    }

    @GetMapping("/project/list")
    public CommonDTO selectProjectList(Pageable pageable, Project project, String search) {
        CommonDTO result = new CommonDTO();
        Page<Project> projectList = projectRepositoryCustom.findAllProject(project, pageable, search);
        result.setContent(projectList);
        return result;
    }

    @PostMapping("/project/registration")
    public CommonDTO inseretProject(Boolean isExistProfile, Project project, Profile profile, MultipartFile profileImg, MultipartFile projectImg, MultipartFile projectAttFile) throws Exception {
        CommonDTO result = new CommonDTO();

        //대표이미지: NULL 체크
        if(Util.isEmpty(projectImg)) {
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_NULL_152);
            return result;
        }
        //제목: NULL체크
        if(Util.isEmpty(project.getProjTitle())){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_NULL_132);
            return result;
        }
        //제목: 길이 제한 체크
        if(!Util.isLengthChk(project.getProjTitle(),0,100)){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_NULL_132);
            return result;
        }
        //상품분류: NULL체크
        if(Util.isEmpty(project.getProjIndus())){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_NULL_127);
            return result;
        }
        //상품단가: NULL체크
        if(Util.isEmpty(project.getProjPrice())){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_NULL_134);
            return result;
        }
        //판매마진: NULL체크
        if(Util.isEmpty(project.getProjMargin())){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_NULL_135);
            return result;
        }
        //등록지역: NULL체크
        if(Util.isEmpty(project.getProjNation())){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_NULL_136);
            return result;
        }
        //공급방법: NULL체크
        if(Util.isEmpty(project.getProjSupplyType())){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_NULL_137);
            return result;
        }
        //모집마감일: NULL체크
        if(Util.isEmpty(project.getProjEndDate())){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_NULL_139);
            return result;
        }
        //모집인원: NULL체크
        if(Util.isEmpty(project.getProjRecruitNum())){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_NULL_140);
            return result;
        }
        //모집인원: 숫자 형식 체크
        if(!Util.isNumeric(project.getProjRecruitNum().toString())){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_FORMAT_142);
            return result;
        }
        //상세설명: NULL체크
        if(Util.isEmpty(project.getProjDetail())){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_NULL_143);
            return result;
        }

        ProjectDto projectDto = new ProjectDto();
        project.setProjId(Util.getUniqueId("P-", Integer.parseInt(project.getProjSort())));
        project.setProjRegDate(new Date());
        project.setProjState("1");
        //현재 썸네일 이미지로 바꾸주는 기능이 없음
        project.setProjThumbnailImg("none");
        projectDto.setProject(project);

        // 프로젝트 해시태그
        if (!Util.isEmpty(project.getProjKeyword())) {
            Hashtag tagProject = new Hashtag();
            tagProject.setFrstRegistDt(new Date());
            tagProject.setFrstRegistMngr(project.getProjMemId());
            tagProject.setHashType("1");
            tagProject.setId(project.getProjId());
            tagProject.setHashNmList(Arrays.asList(project.getProjKeyword().split(",")));

            projectDto.setProjHashtag(tagProject);
        }
        // 프로필 해시태그
        if (!Util.isEmpty(profile.getProfileHashtag())) {
            Hashtag tagProfile = new Hashtag();
            tagProfile.setFrstRegistDt(new Date());
            tagProfile.setFrstRegistMngr(profile.getProfileMemId());
            tagProfile.setHashType("2");
            tagProfile.setId(profile.getProfileId());
            projectDto.setProfileHashtag(tagProfile);
        }

        if(isExistProfile) projectDto.setProfile(profile);
        projectDto.setProfileImgFile(profileImg);
        projectDto.setProjImgFile(projectImg);
        projectDto.setProjAttFile(projectAttFile);

        result.setContent(projectService.insertAndUpdateProject(projectDto));
        return result;
    }

    @PutMapping("/project")
    public CommonDTO updateProject(ProjectDto projectDto, MultipartFile profileImg, MultipartFile projectImg, MultipartFile projectAttFile) {
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
