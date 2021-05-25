package com.sellermatch.process.profile.controller;

import com.sellermatch.process.apply.repositiory.ApplyRepository;
import com.sellermatch.process.common.domain.CommonConstant;
import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.hashtag.domain.Hashtag;
import com.sellermatch.process.hashtag.domain.Hashtaglist;
import com.sellermatch.process.hashtag.repository.HashtagRepository;
import com.sellermatch.process.hashtag.repository.HashtaglistRepository;
import com.sellermatch.process.profile.domain.Profile;
import com.sellermatch.process.profile.repository.ProfileRepository;
import com.sellermatch.process.profile.repository.ProfileRepositoryCustom;
import com.sellermatch.process.profile.service.ProfileService;
import com.sellermatch.process.project.domain.ProjectDto;
import com.sellermatch.process.project.repository.ProjectRepository;
import com.sellermatch.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProfileController {

    private final ProfileRepository profileRepository;
    private final ProfileService profileService;
    private final ProfileRepositoryCustom profileRepositoryCustom;
    private final ProjectRepository projectRepository;
    private final ApplyRepository applyRepository;
    private final HashtagRepository hashtagRepository;
    private final HashtaglistRepository hashtaglistRepository;

    @GetMapping("/profile/{id}")
    public CommonDTO selectProfile(@PathVariable Integer id) {
        CommonDTO result = new CommonDTO();
        profileRepository.findById(id).ifPresentOrElse(temp -> {
            result.setContent(temp);
        }, () -> {
            result.setResult("ERROR");
            result.setStatus(CommonConstant.ERROR_998);
            result.setContent(new Profile());
        });
        return result;
    }

    @GetMapping("/profile/list")
    public CommonDTO selectProfileList(Pageable pageable, Profile profile) {
        CommonDTO result = new CommonDTO();
        List<Profile> profileList = new ArrayList<>();
        profileList = profileRepositoryCustom.findAllSeller(profile);
        profileList.forEach(profileDTO -> {
            //profileDTO.setProfileIndusName();
            String APPLYTYPE = "2";
            String APPLYPROJSTATE = "5";
//            profileDTO.setProjAddCount(projectRepository.countByProjMemId(profileDTO.getMember().getMemId()));
//            profileDTO.setRecommendCount(applyRepository.countByApplyMemIdAndApplyType(profileDTO.getMember().getMemId(), APPLYTYPE));
//            profileDTO.setContractCount(applyRepository.countByApplyMemIdAndApplyProjState(profileDTO.getMember().getMemId(), APPLYPROJSTATE));

            Hashtag hashtag = hashtagRepository.findById(profileDTO.getProfileId());
            int hashtag1 = hashtag.getHashTag1();
            int hashtag2 = hashtag.getHashTag2();
            int hashtag3 = hashtag.getHashTag3();
            int hashtag4 = hashtag.getHashTag4();
            int hashtag5 = hashtag.getHashTag5();
            Hashtaglist hashtaglist1 = hashtaglistRepository.findByHashId(hashtag1);
            Hashtaglist hashtaglist2 = hashtaglistRepository.findByHashId(hashtag2);
            Hashtaglist hashtaglist3 = hashtaglistRepository.findByHashId(hashtag3);
            Hashtaglist hashtaglist4 = hashtaglistRepository.findByHashId(hashtag4);
            Hashtaglist hashtaglist5 = hashtaglistRepository.findByHashId(hashtag5);

            profileDTO.setHashTag1(hashtaglist1.getHashNm());
            profileDTO.setHashTag2(hashtaglist2.getHashNm());
            profileDTO.setHashTag3(hashtaglist3.getHashNm());
            profileDTO.setHashTag4(hashtaglist4.getHashNm());
            profileDTO.setHashTag5(hashtaglist5.getHashNm());
        });
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
    public CommonDTO updateProfile(@RequestBody ProjectDto projectDto, MultipartFile profileImg) {
        CommonDTO result = new CommonDTO();
        Profile profile = projectDto.getProfile();
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
        profileRepository.findById(projectDto.getProfile().getProfileIdx()).ifPresentOrElse(temp ->{
            projectDto.setProfileImgFile(profileImg);
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
