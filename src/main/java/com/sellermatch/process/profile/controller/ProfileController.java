package com.sellermatch.process.profile.controller;

import com.sellermatch.process.common.domain.CommonConstant;
import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.profile.domain.Profile;
import com.sellermatch.process.profile.repository.ProfileRepository;
import com.sellermatch.process.profile.repository.ProfileRepositoryCustom;
import com.sellermatch.process.profile.service.ProfileService;
import com.sellermatch.process.project.domain.ProjectDto;
import com.sellermatch.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api-v1")
public class ProfileController {

    private final ProfileRepository profileRepository;
    private final ProfileService profileService;
    private final ProfileRepositoryCustom profileRepositoryCustom;

    @GetMapping("/profile/{id}")
    public CommonDTO  selectProfile(@PathVariable Integer id) {
        CommonDTO result = new CommonDTO();
        Profile profile = profileRepositoryCustom.findSeller(id);
        if (profile != null) {
            result.setContent(profile);
        } else {
            result.setResult("ERROR");
            result.setStatus(CommonConstant.ERROR_998);
            result.setContent(new Profile());
        }
        return result;
    }

    @GetMapping("/profile/list")
    public CommonDTO selectProfileList(Pageable pageable, Profile profile, String search) {
        CommonDTO result = new CommonDTO();
        Page<Profile> profileList = profileRepositoryCustom.findAllSeller(profile, pageable, search);
        result.setContent(profileList);
        return result;
    }

    @PostMapping("/profile")
    public CommonDTO insertProfile(@RequestBody ProjectDto projectDto, MultipartFile profileImg) throws Exception {
        CommonDTO result = new CommonDTO();
        projectDto.setProfileImgFile(profileImg);
        result.setContent(profileService.insertAndUpdateProfile(projectDto));
        return result;
    }

    @PutMapping("/profile")
    public CommonDTO updateProfile(Profile profile, MultipartFile profileImg) {
        CommonDTO result = new CommonDTO();
        // 자기소개 : NULL 체크
        if (Util.isEmpty(profile.getProfileIntro())) {
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_NULL_121);
            return result;
        }
        // 자기소개 : 길이 체크 (10자 이상 1000자 이하)
        if (!Util.isLengthChk(profile.getProfileIntro(), 10 , 1000)) {
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_LENGTH_122);
            return result;
        }
        // 매출규모 : NULL 체크
        if (Util.isEmpty(profile.getProfileVolume())){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_NULL_123);
        }
        // 매출규모 : 숫자형식 체크
        if (!Util.isNum(String.valueOf(profile.getProfileVolume()))){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_FORMAT_124);
        }
        // 매출규모 : 길이 체크 ( 0원 부터 1조원 까지)
        if (!Util.isLengthChk(String.valueOf(profile.getProfileVolume()),0,13)) {
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_LENGTH_125);
        }
        // 등록지역 : NULL 체크
        if (Util.isEmpty(profile.getProfileNation())) {
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_NULL_126);
        }
        // 상품분류 : NULL 체크
        if (Util.isEmpty(profile.getProfileIndus())){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_NULL_127);
        }
        // 사업자번호 : NULL 체크
        if (Util.isEmpty(profile.getProfileBizNum())){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_NULL_128);
        }
        // 사업자번호 : 사업자번호형식 체크
        if (!Util.isNumeric(profile.getProfileBizNum())){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_FORMAT_129);
        }
        // 사업자유형 : NULL 체크
        if (Util.isEmpty(profile.getProfileBizSort())) {
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_NULL_130);
        }
        profileRepository.findById(profile.getProfileIdx()).ifPresentOrElse(temp -> {
            ProjectDto projectDto = new ProjectDto();
            projectDto.setProfile(profile);
            if(!Util.isEmpty(profileImg)) projectDto.setProfileImgFile(profileImg);
            projectDto.getProfile().setProfileEditDate(new Date());
            try {
                result.setContent(profileService.insertAndUpdateProfile(projectDto));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, () -> {});
        return result;
    }

/*    @DeleteMapping("/profile")
    public CommonDTO deleteProfile(Profile profile) {
        CommonDTO result = new CommonDTO();
        profileRepository.findById(profile.getProfileIdx()).ifPresentOrElse(temp ->{
            profileRepository.delete(profile);
        }, () -> {});
        return result;
    }*/
}
