package com.sellermatch.process.profile.service;

import com.sellermatch.config.constant.HashtagType;
import com.sellermatch.process.file.domain.File;
import com.sellermatch.process.file.service.FileService;
import com.sellermatch.process.hashtag.domain.Hashtag;
import com.sellermatch.process.hashtag.repository.HashtagRepository;
import com.sellermatch.process.hashtag.service.HashtagService;
import com.sellermatch.process.profile.domain.Profile;
import com.sellermatch.process.profile.repository.ProfileRepository;
import com.sellermatch.process.project.domain.ProjectDto;
import com.sellermatch.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileService {

    private final FileService fileService;
    private final HashtagService hashtagService;
    private final ProfileRepository profileRepository;
    private final HashtagRepository hashtagRepository;

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class}, timeout = 1000)
    public Profile insertAndUpdateProfile(ProjectDto projectDto) throws Exception {
        if(projectDto.getProfileImgFile() != null && !projectDto.getProfileImgFile().isEmpty()) {
            File file = new File();
            file.setProfileId(projectDto.getProfile().getProfileId());
            file = fileService.insertFile(projectDto.getProfileImgFile(),file);
            projectDto.getProfile().setProfilePhoto(file.getFilePath());
        }
        if(!Util.isEmpty(projectDto.getProfile().getProfileHashtag())) {
            Hashtag hashtag = new Hashtag();
            hashtagRepository.findById(projectDto.getProfile().getProfileId()).ifPresentOrElse(temp -> {
                hashtag.setFrstRegistDt(temp.getFrstRegistDt());
                hashtag.setFrstRegistMngr(temp.getFrstRegistMngr());
                hashtag.setLastRegistDt(new Date());
                hashtag.setLastRegistMngr(projectDto.getProfile().getProfileId());
                hashtag.setHashType(temp.getHashType());
                hashtag.setId(temp.getId());
                hashtag.setHashNmList(Arrays.asList(projectDto.getProfile().getProfileHashtag().split(",")));
            }, ()->{
                hashtag.setFrstRegistDt(new Date());
                hashtag.setFrstRegistMngr(projectDto.getProfile().getProfileMemId());
                hashtag.setHashType(HashtagType.PROFILE.label);
                hashtag.setId(projectDto.getProfile().getProfileId());
                hashtag.setHashNmList(Arrays.asList(projectDto.getProfile().getProfileHashtag().split(",")));
            });

            hashtagService.insertAndUpdateHashtag(hashtag);
        } else {
            Hashtag hashtag = new Hashtag();
            hashtagRepository.findById(projectDto.getProfile().getProfileId()).ifPresent(temp -> {
                hashtag.setFrstRegistDt(temp.getFrstRegistDt());
                hashtag.setFrstRegistMngr(temp.getFrstRegistMngr());
                hashtag.setLastRegistDt(new Date());
                hashtag.setLastRegistMngr(projectDto.getProfile().getProfileId());
                hashtag.setHashType(temp.getHashType());
                hashtag.setId(temp.getId());
                hashtag.setHashTag1(null);
                hashtag.setHashTag2(null);
                hashtag.setHashTag3(null);
                hashtag.setHashTag4(null);
                hashtag.setHashTag5(null);
            });
            hashtagService.insertAndUpdateHashtag(hashtag);
        }
        profileRepository.save(projectDto.getProfile());
        return projectDto.getProfile();
    }
}
