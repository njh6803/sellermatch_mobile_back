package com.sellermatch.process.project.controller;

import com.sellermatch.config.constant.HashtagType;
import com.sellermatch.config.constant.ProjectStateType;
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
import com.sellermatch.util.ControllerResultSet;
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
    public CommonDTO selectProject(@PathVariable Integer id, Integer memIdx) {
        CommonDTO result = new CommonDTO();

        if (Util.isEmpty(memIdx)) {
            memIdx = 0;
        }

        Project project = projectRepositoryCustom.findProject(id, memIdx);
        if (project != null) {
            result.setContent(project);
        } else {
            Profile emptyContent =  new Profile();
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_998, emptyContent);
        }
        return result;
    }

    @GetMapping("/project/list/{profileMemId}")
    public CommonDTO getProject(@PathVariable String profileMemId, Pageable pageable) {
        CommonDTO result = new CommonDTO();
        // ?????????????????????(????????? ??????) - ????????? ?????????????????? ????????? ??? ??????
        Page<Project> project = projectRepository.findAllByProjMemIdAndProjStateNot(profileMemId, ProjectStateType.STOP.label, pageable);
        result.setContent(project);
        return result;
    }

    @GetMapping("/project/list/recommend/{memIdx}")
    public CommonDTO selectRecommendProject(@PathVariable Integer memIdx, Pageable pageable) {
        CommonDTO result = new CommonDTO();
        Project project = new Project();

        //???????????? ???????????? ??????????????? ???????????? ????????? ???????????? ????????? "Y"??? ???????????? ?????? ?????????
        String recommandProjectFlag = "Y";
        memberRepository.findById(memIdx).ifPresentOrElse(temp -> {
            project.setProjMemId(temp.getMemId());
            project.setRecommandProjectFlag(recommandProjectFlag);
            Page<Project> projectList = projectRepositoryCustom.findAllProject(project, 0, pageable, null);
            result.setContent(projectList);

            if (Util.isEmpty(projectList)) {
                ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_216);
            }
        }, ()->{});
        return result;
    }

    @GetMapping("/project/list")
    public CommonDTO selectProjectList(Integer memIdx, Pageable pageable, Project project, String search) {
        CommonDTO result = new CommonDTO();

        if (Util.isEmpty(memIdx)) {
            memIdx = 0;
        }

        if (Util.isEmpty(project.getEndProjectFlag())) {
            project.setEndProjectFlag("N");
        }

        Page<Project> projectList = projectRepositoryCustom.findAllProject(project, memIdx, pageable, search);
        for (int i = 0; i < projectList.getContent().size(); i++) {
            projectList.getContent().get(i).setEndProjectFlag(project.getEndProjectFlag());
        }
        result.setContent(projectList);
        return result;
    }

    @PostMapping("/project/registration")
    public CommonDTO inseretProject(Boolean isExistProfile, Project project, Profile profile, MultipartFile profileImg, MultipartFile projectImg, MultipartFile projectAttFile) throws Exception {
        CommonDTO result = new CommonDTO();
        //???????????????: NULL ??????
        if(Util.isEmpty(projectImg) || projectImg.isEmpty() || projectImg.getSize() == 0) {
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_152);
            return result;
        } else {
            if(projectImg.getContentType().indexOf("image") == -1) { //???????????????: ??????????????? ??????
                ControllerResultSet.errorCode(result, CommonConstant.ERROR_MISMATCH_224);
                return result;
            }
        }
        //??????: NULL??????
        if(Util.isEmpty(project.getProjTitle())){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_132);
            return result;
        }
        //??????: ?????? ?????? ??????
        /*if(!Util.isLengthChk(project.getProjTitle(),0,100)){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_LENGTH_133);
            return result;
        }*/
        //????????????: NULL??????
        if(Util.isEmpty(project.getProjIndus())){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_127);
            return result;
        }
        //????????????: NULL??????
        if(Util.isEmpty(project.getProjPrice())){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_134);
            return result;
        }
        //????????????: NULL??????
        if(Util.isEmpty(project.getProjMargin()) || project.getProjMargin() == 0){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_135);
            return result;
        }
        //????????????: NULL??????
        if (Util.isEmpty(project.getProjChannel())) {
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_138);
            return result;
        }
        //????????????: NULL??????
        if(Util.isEmpty(project.getProjSupplyType())){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_137);
            return result;
        }
        //????????????: NULL??????
        if(Util.isEmpty(project.getProjNation())){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_136);
            return result;
        }
        //????????????: NULL??????
        if(Util.isEmpty(project.getProjRecruitNum())){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_140);
            return result;
        }
        //????????????: ?????? ??????
        if(project.getProjRecruitNum() <= 0 ){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NUMSIZE_217);
            return result;
        }
        //????????????: ?????? ??????
        if(project.getProjRecruitNum() > 100 ){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NUMSIZE_218);
            return result;
        }
        //????????????: ?????? ?????? ??????
        if(!Util.isNumeric(project.getProjRecruitNum().toString())){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_FORMAT_142);
            return result;
        }
        //????????????: ?????? ?????? ??????
        if(project.getProjRecruitNum()<1 && project.getProjRecruitNum()>100){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NUMSIZE_141);
            return result;
        }
        //???????????????: NULL??????
        if(Util.isEmpty(project.getProjEndDate())){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_139);
            return result;
        }
        //????????????: NULL??????
        if(Util.isEmpty(project.getTagRemoveProjDetail())){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_143);
            return result;
        }
        //????????????: ?????? ?????? ??????
        /*if(Util.isLengthChk(project.getProjDetail(), 50, 1000)){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_LENGTH_151);
            return result;
        }*/
        // ???????????? ???????????? ????????????
        if (!Util.isEmpty(project.getProjKeyword())) {
            if (project.getProjKeyword().trim().length() > 0) {
                String[] hashtagList = project.getProjKeyword().split(",");
                for (int i = 0; i < hashtagList.length; i++) {
                    hashtagList[i] = hashtagList[i].trim();
                    hashtagList[i] = hashtagList[i].replace(" ","");
                }
                if (Util.hashtagDuplicateCheck(result, hashtagList)) return result;
            }
        }
        // ????????? ???????????????-----------------------------------------------------------------------------------
        if (isExistProfile) {
            // ???????????? : NULL ??????
            if (Util.isEmpty(profile.getTagRemoveProfileIntro())) {
                ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_121);
                return result;
            }
            // ???????????? : ?????? ?????? (50??? ?????? 1000??? ??????)
            /*if (Util.isLengthChk(profile.getProfileIntro(), 50 , 1000)) {
                ControllerResultSet.errorCode(result, CommonConstant.ERROR_LENGTH_122);
                return result;
            }*/
            // ???????????? : NULL ??????
            if (Util.isEmpty(profile.getProfileVolume())){
                ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_123);
                return result;
            }
            // ???????????? : ???????????? ??????
            if (!Util.isNum(String.valueOf(profile.getProfileVolume()))){
                ControllerResultSet.errorCode(result, CommonConstant.ERROR_FORMAT_124);
                return result;
            }
            // ???????????? : ?????? ?????? ( 0??? ?????? 1?????? ??????)
            if (!Util.isLengthChk(String.valueOf(profile.getProfileVolume()),0,13)) {
                ControllerResultSet.errorCode(result, CommonConstant.ERROR_LENGTH_125);
                return result;
            }
            // ???????????? : NULL
            if (Util.isEmpty(profile.getProfileCareer())) {
                ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_235);
                return result;
            }
            // ???????????? : NULL
            if (Util.isEmpty(profile.getProfileCh())) {
                ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_236);
                return result;
            }
            // ???????????? : NULL ??????
            if (Util.isEmpty(profile.getProfileNation())) {
                ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_126);
                return result;
            }
            // ???????????? : NULL ??????
            if (Util.isEmpty(profile.getProfileIndus())){
                ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_127);
                return result;
            }
            // ??????????????? : NULL ??????
            if (Util.isEmpty(profile.getProfileBizNum())){
                ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_128);
                return result;
            }
            // ??????????????? : ????????????????????? ??????
            if (!Util.isEmpty(profile.getProfileBizNum()) && !Util.isValid(profile.getProfileBizNum())){
                ControllerResultSet.errorCode(result, CommonConstant.ERROR_FORMAT_129);
                return result;
            }
            // ??????????????? : NULL ??????
            if (Util.isEmpty(profile.getProfileBizSort())) {
                ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_130);
                return result;
            }
            // ????????? ???????????? ????????????
            if (!Util.isEmpty(profile.getProfileHashtag())) {
                if (profile.getProfileHashtag().trim().length() > 0) {
                    String[] hashtagList = profile.getProfileHashtag().split(",");
                    for (int i = 0; i < hashtagList.length; i++) {
                        hashtagList[i] = hashtagList[i].trim();
                        hashtagList[i] = hashtagList[i].replace(" ","");
                    }
                    if (Util.hashtagDuplicateCheck(result, hashtagList)) return result;
                }
            }
        }

        ProjectDto projectDto = new ProjectDto();
        project.setProjId(Util.getUniqueId("P-", Integer.parseInt(project.getProjSort())));
        project.setProjRegDate(new Date());
        project.setProjState(ProjectStateType.NORMAL.label);
        projectDto.setProject(project);
        project.setProjThumbnailImg("none");
        project.setProjProdCerti("0");
        project.setProjProfit("0");

        // ???????????? ????????????
        if (!Util.isEmpty(project.getProjKeyword())) {
            Hashtag tagProject = new Hashtag();
            tagProject.setFrstRegistDt(new Date());
            tagProject.setFrstRegistMngr(project.getProjMemId());
            tagProject.setHashType(HashtagType.PROJECT.label);
            tagProject.setId(project.getProjId());
            tagProject.setHashNmList(Arrays.asList(project.getProjKeyword().split(",")));

            projectDto.setProjHashtag(tagProject);
        }
        // ????????? ????????????
        if (!Util.isEmpty(profile.getProfileHashtag())) {
            Hashtag tagProfile = new Hashtag();
            tagProfile.setFrstRegistDt(new Date());
            tagProfile.setFrstRegistMngr(profile.getProfileMemId());
            tagProfile.setHashType(HashtagType.PROFILE.label);
            tagProfile.setId(profile.getProfileId());
            projectDto.setProfileHashtag(tagProfile);
        }

        if(isExistProfile) {
            projectDto.setProfile(profile);
            projectDto.setProfileImgFile(profileImg);
            projectDto.getProfile().setProfileEditDate(new Date());
        }
        projectDto.setProjImgFile(projectImg);
        projectDto.setProjAttFile(projectAttFile);

        result.setContent(projectService.insertAndUpdateProject(projectDto));
        return result;
    }

    @PutMapping("/project/modify")
    public CommonDTO updateProject(Project project, MultipartFile projectImg, MultipartFile projectAttFile) {
        CommonDTO result = new CommonDTO();
        // ??????????????? NULL ??????
        if(Util.isEmpty(projectImg) || projectImg.isEmpty() || projectImg.getSize() == 0) {
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_152);
        }
        //??????: NULL??????
        if(Util.isEmpty(project.getProjTitle())){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_132);
            return result;
        }
        //??????: ?????? ?????? ??????
        /*if(!Util.isLengthChk(project.getProjTitle(),0,100)){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_LENGTH_133);
            return result;
        }*/
        //????????????: NULL??????
        if(Util.isEmpty(project.getProjIndus())){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_127);
            return result;
        }
        //????????????: NULL??????
        if(Util.isEmpty(project.getProjPrice())){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_134);
            return result;
        }
        //????????????: NULL??????
        if(Util.isEmpty(project.getProjMargin()) || project.getProjMargin() == 0){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_135);
            return result;
        }
        //????????????: NULL??????
        if (Util.isEmpty(project.getProjChannel())) {
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_138);
            return result;
        }
        //????????????: NULL??????
        if(Util.isEmpty(project.getProjSupplyType())){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_137);
            return result;
        }
        //????????????: NULL??????
        if(Util.isEmpty(project.getProjNation())){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_136);
            return result;
        }
        //????????????: NULL??????
        if(Util.isEmpty(project.getProjRecruitNum())){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_140);
            return result;
        }
        //????????????: ?????? ??????
        if(project.getProjRecruitNum() <= 0 ){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NUMSIZE_217);
            return result;
        }
        //????????????: ?????? ??????
        if(project.getProjRecruitNum() > 100 ){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NUMSIZE_218);
            return result;
        }
        //????????????: ?????? ?????? ??????
        if(!Util.isNumeric(project.getProjRecruitNum().toString())){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_FORMAT_142);
            return result;
        }
        //????????????: ?????? ?????? ??????
        if(project.getProjRecruitNum()<1 && project.getProjRecruitNum()>100){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NUMSIZE_141);
            return result;
        }
        //???????????????: NULL??????
        if(Util.isEmpty(project.getProjEndDate())){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_139);
            return result;
        }
        //????????????: NULL??????
        if(Util.isEmpty(project.getTagRemoveProjDetail())){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_143);
            return result;
        }
        //????????????: ?????? ?????? ??????
        /*if(Util.isLengthChk(project.getProjDetail(), 50, 1000)){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_LENGTH_151);
            return result;
        }*/
        // ???????????? ???????????? ????????????
        if (!Util.isEmpty(project.getProjKeyword())) {
            if (project.getProjKeyword().trim().length() > 0) {
                String[] hashtagList = project.getProjKeyword().split(",");
                for (int i = 0; i < hashtagList.length; i++) {
                    hashtagList[i] = hashtagList[i].trim();
                    hashtagList[i] = hashtagList[i].replace(" ","");
                }
                if (Util.hashtagDuplicateCheck(result, hashtagList)) return result;
            }
        }

        projectRepository.findById(project.getProjIdx()).ifPresentOrElse(temp -> {
            //???????????????: NULL ??????
            if(Util.isEmpty(projectImg) || projectImg.isEmpty() || projectImg.getSize() == 0) {
                ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_152);
            } else {
                ProjectDto projectDto = new ProjectDto();

                temp.setProjTitle(project.getProjTitle());
                temp.setProjIndus(project.getProjIndus());
                temp.setProjPrice(project.getProjPrice());
                temp.setProjMargin(project.getProjMargin());
                temp.setProjChannel(project.getProjChannel());
                temp.setProjSupplyType(project.getProjSupplyType());
                temp.setProjNation(project.getProjNation());
                temp.setProjRecruitNum(project.getProjRecruitNum());
                temp.setProjEndDate(project.getProjEndDate());
                temp.setProjDetail(project.getProjDetail());
                temp.setProjRequire(project.getProjRequire());
                temp.setProjKeyword(project.getProjKeyword());
                projectDto.setProject(temp);

                projectDto.setProjImgFile(projectImg);

                try {
                    result.setContent(projectService.updateAndUpdateProject(projectDto));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, () -> {});
        return result;
    }

    @PutMapping("/project/close")
    public CommonDTO updateProjectStatus(@RequestBody Project project) {
        CommonDTO result = new CommonDTO();
        projectRepository.findById(project.getProjIdx()).ifPresent(temp -> {
            temp.setProjState(ProjectStateType.END.label);
            temp.setProjEndDate(new Date());
            temp.setProjEditDate(new Date());
            result.setContent(projectRepository.save(temp));
        });
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
