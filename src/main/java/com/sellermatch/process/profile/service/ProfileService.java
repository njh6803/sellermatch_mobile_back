package com.sellermatch.process.profile.service;

import com.sellermatch.process.file.domain.File;
import com.sellermatch.process.file.service.FileService;
import com.sellermatch.process.hashtag.service.HashtagService;
import com.sellermatch.process.profile.domain.Profile;
import com.sellermatch.process.profile.repository.ProfileRepository;
import com.sellermatch.process.project.domain.ProjectDto;
import com.sellermatch.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileService {

    @Autowired
    FileService fileService;

    @Autowired
    HashtagService hashtagService;

    @Autowired
    private ProfileRepository profileRepository;

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class}, timeout = 1000)
    public Profile insertAndUpdateProfile(ProjectDto projectDto) throws Exception {
        if(projectDto.getProfileImgFile() != null && !projectDto.getProfileImgFile().isEmpty()) {
            File file = new File();
            file.setProfileId(projectDto.getProfile().getProfileId());
            file = fileService.insertFile(projectDto.getProfileImgFile(),file);
            projectDto.getProfile().setProfilePhoto(file.getFilePath());
        }
        if(!Util.isEmpty(projectDto.getProfileHashtag())) {
            projectDto.getProfileHashtag().setId(projectDto.getProfile().getProfileId());
            projectDto.getProfileHashtag().setHashNmList(Arrays.asList(projectDto.getProfile().getProfileHashtag().split(",")));
            hashtagService.insertAndUpdateHashtag(projectDto.getProfileHashtag());
        }
        profileRepository.save(projectDto.getProfile());
        return projectDto.getProfile();
    }
}
