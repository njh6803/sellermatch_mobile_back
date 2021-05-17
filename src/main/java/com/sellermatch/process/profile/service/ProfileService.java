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

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public Profile insertAndUpdateProfile(ProjectDto projectDto) throws Exception {
        Profile profile= profileRepository.save(projectDto.getProfile());

        if(!Util.isEmpty(projectDto.getProfileImgFile())) {
            File file = new File();
            file.setProfileId(profile.getProfileId());
            fileService.insertFile(projectDto.getProfileImgFile(),file);
        }
        if(!Util.isEmpty(projectDto.getProfileHashtag())) {
            projectDto.getProjHashtag().setId(profile.getProfileId());
            hashtagService.insertAndUpdateHashtag(projectDto.getProfileHashtag());
        }
        return projectDto.getProfile();
    }
}
