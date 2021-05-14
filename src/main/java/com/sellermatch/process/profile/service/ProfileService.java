package com.sellermatch.process.profile.service;

import com.sellermatch.process.file.domain.File;
import com.sellermatch.process.file.service.FileService;
import com.sellermatch.process.profile.domain.Profile;
import com.sellermatch.process.profile.repository.ProfileRepository;
import com.sellermatch.process.project.domain.ProjectDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileService {

    @Autowired
    FileService fileService;

    @Autowired
    private ProfileRepository profileRepository;

    @Transactional
    public Profile insertProfile(ProjectDto projectDto) throws Exception {
        profileRepository.save(projectDto.getProfile());
        File fileDto = new File();
        fileDto.setProfileId(projectDto.getProfile().getProfileId());
        fileService.insertFile(projectDto.getProfileImgFile(),projectDto.getFile());
        return projectDto.getProfile();
    }
}
