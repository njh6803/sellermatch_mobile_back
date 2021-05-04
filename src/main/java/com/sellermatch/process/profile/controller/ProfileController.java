package com.sellermatch.process.profile.controller;

import com.sellermatch.process.profile.domain.Profile;
import com.sellermatch.process.profile.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProfileController {

    @Autowired
    public ProfileRepository profileRepository;

    @GetMapping("/profile")
    public List<Profile> selectProfile() {
        return profileRepository.findAll();
    }
}
