package com.sellermatch.process.profile.controller;

import com.sellermatch.process.common.domain.CommonConstant;
import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.profile.domain.Profile;
import com.sellermatch.process.profile.repository.ProfileRepository;
import com.sellermatch.process.profile.repository.ProfileRepositoryCustom;
import com.sellermatch.process.profile.service.ProfileService;
import com.sellermatch.process.project.domain.ProjectDto;
import com.sellermatch.util.ControllerResultSet;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        profileRepository.findById(profile.getProfileIdx()).ifPresentOrElse(temp -> {
            // 유효성 체크 확인
            ProjectDto projectDto = new ProjectDto();
            projectDto.setProfile(profile);
            if(profileImg != null && !profileImg.isEmpty()) projectDto.setProfileImgFile(profileImg);
            try {
                result.setContent(profileService.insertAndUpdateProfile(projectDto));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, () -> {});
        return result;
    }

}
