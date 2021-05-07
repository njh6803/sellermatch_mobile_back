package com.sellermatch.process.profile.controller;

import com.sellermatch.process.profile.domain.Profile;
import com.sellermatch.process.profile.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
public class ProfileController {

    @Autowired
    public ProfileRepository profileRepository;

    @GetMapping("/profile")
    public Page<Profile> selectProfile() {
        Pageable pageable = PageRequest.of(0,1);
        return profileRepository.findAll(pageable);
    }

    @GetMapping("/profile/list")
    public Page<Profile> selectProfileList(Pageable pageable) {
        return profileRepository.findAll(pageable);
    }

    @PostMapping("/profile")
    public Profile insertProfile(Profile profile) {
        return profileRepository.save(profile);
    }

    @PutMapping("/profile")
    public Profile updateProfile(Profile profile) {
        profileRepository.findById(profile.getProfileIdx()).ifPresentOrElse(temp ->{
            profileRepository.save(profile);
        }, () -> {});
        return profile;
    }

    @DeleteMapping("/profile")
    public Profile deleteProfile(Profile profile) {
        profileRepository.findById(profile.getProfileIdx()).ifPresentOrElse(temp ->{
            profileRepository.delete(profile);
        }, () -> {});
        return profile;
    }
}
