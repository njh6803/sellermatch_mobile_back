package com.sellermatch.process.profile.controller;

import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.profile.domain.Profile;
import com.sellermatch.process.profile.repository.ProfileRepository;
import com.sellermatch.process.profile.service.ProfileService;
import com.sellermatch.process.project.domain.ProjectDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
public class ProfileController {

    @Autowired
    public ProfileRepository profileRepository;

    @Autowired
    private ProfileService profileService;

    @GetMapping("/profile")
    public CommonDTO selectProfile() {
        CommonDTO result = new CommonDTO();
        Pageable pageable = PageRequest.of(0,1);
        result.setContent(profileRepository.findAll(pageable));
        return result;
    }

    @GetMapping("/profile/list")
    public CommonDTO selectProfileList(Pageable pageable) {
        CommonDTO result = new CommonDTO();
        result.setContent(profileRepository.findAll(pageable));
        return result;
    }

    @PostMapping("/profile")
    public CommonDTO insertProfile(ProjectDto projectDto, MultipartFile profileImg) throws Exception {
        CommonDTO result = new CommonDTO();
        projectDto.setProfileImgFile(profileImg);
        result.setContent(profileService.insertAndUpdateProfile(projectDto));
        return result;
    }

    @PutMapping("/profile")
    public CommonDTO updateProfile(ProjectDto projectDto, MultipartFile profileImg) {
        CommonDTO result = new CommonDTO();
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

    @DeleteMapping("/profile")
    public CommonDTO deleteProfile(Profile profile) {
        CommonDTO result = new CommonDTO();
        profileRepository.findById(profile.getProfileIdx()).ifPresentOrElse(temp ->{
            profileRepository.delete(profile);
        }, () -> {});
        return result;
    }
}
