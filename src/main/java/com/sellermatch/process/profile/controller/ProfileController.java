package com.sellermatch.process.profile.controller;

import com.sellermatch.config.constant.MemberType;
import com.sellermatch.process.common.domain.CommonConstant;
import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.profile.domain.Profile;
import com.sellermatch.process.profile.repository.ProfileRepository;
import com.sellermatch.process.profile.repository.ProfileRepositoryCustom;
import com.sellermatch.process.profile.service.ProfileService;
import com.sellermatch.process.project.domain.ProjectDto;
import com.sellermatch.util.ControllerResultSet;
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
            Profile emptyContent =  new Profile();
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_998, emptyContent);
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
    public CommonDTO insertProfile(Profile profile, MultipartFile profileImg) throws Exception {
        CommonDTO result = new CommonDTO();
        ProjectDto projectDto = new ProjectDto();
        projectDto.setProfile(profile);
        projectDto.setProfileImgFile(profileImg);
        result.setContent(profileService.insertAndUpdateProfile(projectDto));
        return result;
    }

    @PutMapping("/profile")
    public CommonDTO updateProfile(Profile profile, MultipartFile profileImg) {
        CommonDTO result = new CommonDTO();
        // 자기소개 : NULL 체크
        if (Util.isEmpty(profile.getTagRemoveProfileIntro())) {
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_121);
            return result;
        }
        // 자기소개 : 길이 체크 (50자 이상 1000자 이하)
            /*if (Util.isLengthChk(profile.getProfileIntro(), 50 , 1000)) {
                ControllerResultSet.errorCode(result, CommonConstant.ERROR_LENGTH_122);
                return result;
            }*/
        if (profile.getProfileSort().equalsIgnoreCase(MemberType.SELLER.label)) {
            // 매출규모 : NULL 체크
            if (Util.isEmpty(profile.getProfileVolume())){
                ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_123);
                return result;
            }
            // 매출규모 : 숫자형식 체크
            if (!Util.isNum(String.valueOf(profile.getProfileVolume()))){
                ControllerResultSet.errorCode(result, CommonConstant.ERROR_FORMAT_124);
                return result;
            }
            // 매출규모 : 길이 체크 ( 0원 부터 1조원 까지)
            if (!Util.isLengthChk(String.valueOf(profile.getProfileVolume()),0,13)) {
                ControllerResultSet.errorCode(result, CommonConstant.ERROR_LENGTH_125);
                return result;
            }
            // 판매경력 : NULL
            if (Util.isEmpty(profile.getProfileCareer())) {
                ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_235);
                return result;
            }
            // 판매채널 : NULL
            if (Util.isEmpty(profile.getProfileCh())) {
                ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_236);
                return result;
            }
        }
        // 등록지역 : NULL 체크
        if (Util.isEmpty(profile.getProfileNation())) {
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_126);
            return result;
        }
        // 상품분류 : NULL 체크
        if (Util.isEmpty(profile.getProfileIndus())){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_127);
            return result;
        }
        // 사업자번호 : NULL 체크
        if (Util.isEmpty(profile.getProfileBizNum())){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_128);
            return result;
        }
        // 사업자번호 : 사업자번호형식 체크
        if (!Util.isEmpty(profile.getProfileBizNum()) && !Util.isValid(profile.getProfileBizNum())){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_FORMAT_129);
            return result;
        }
        // 사업자유형 : NULL 체크
        if (Util.isEmpty(profile.getProfileBizSort())) {
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_130);
            return result;
        }
        if (profile.getProfileSort().equalsIgnoreCase(MemberType.SELLER.label)) {
            // 프로필 해시태그 중복체크
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

        profileRepository.findById(profile.getProfileIdx()).ifPresentOrElse(temp -> {
            ProjectDto projectDto = new ProjectDto();
            projectDto.setProfile(temp);

            temp.setProfileIntro(profile.getProfileIntro());
            temp.setProfileNation(profile.getProfileNation());
            temp.setProfileIndus(profile.getProfileIndus());
            temp.setProfileBizSort(profile.getProfileBizSort());
            temp.setProfileEditDate(new Date());
            if (!Util.isEmpty(profile.getProfileBizNum())) {
                temp.setProfileBizNum(profile.getProfileBizNum());
            }
            if (profile.getProfileSort().equalsIgnoreCase(MemberType.SELLER.label)) {
                temp.setProfileVolume(profile.getProfileVolume());
                temp.setProfileCareer(profile.getProfileCareer());
                temp.setProfileCh(profile.getProfileCh());
                // 프로필 해시태그
                projectDto.getProfile().setProfileHashtag(profile.getProfileHashtag());
            }
            if(profileImg != null && !profileImg.isEmpty()) {
                projectDto.setProfileImgFile(profileImg);
            }
            projectDto.setProfile(temp);
            try {
                result.setContent(profileService.insertAndUpdateProfile(projectDto));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, () -> {
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_998);
        });
        return result;
    }

}
