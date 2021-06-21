package com.sellermatch.process.project.service;

import com.sellermatch.config.constant.HashtagType;
import com.sellermatch.process.file.domain.File;
import com.sellermatch.process.file.service.FileService;
import com.sellermatch.process.hashtag.domain.Hashtag;
import com.sellermatch.process.hashtag.repository.HashtagRepository;
import com.sellermatch.process.hashtag.service.HashtagService;
import com.sellermatch.process.profile.repository.ProfileRepository;
import com.sellermatch.process.profile.service.ProfileService;
import com.sellermatch.process.project.domain.Project;
import com.sellermatch.process.project.domain.ProjectDto;
import com.sellermatch.process.project.repository.ProjectRepository;
import com.sellermatch.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectService {

    private final FileService fileService;
    private final HashtagService hashtagService;
    private final ProfileService profileService;
    private final ProjectRepository projectRepository;
    private final ProfileRepository profileRepository;
    private final HashtagRepository hashtagRepository;

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class}, timeout = 1000)
    public Project insertAndUpdateProject(ProjectDto projectDto) throws Exception {
        Project project = projectRepository.save(projectDto.getProject());
        //프로젝트 이미지 있을 경우 첨부
        if(!Util.isEmpty(projectDto.getProjImgFile())) {
            File file = new File();
            file.setProjId(project.getProjId());
            file.setProjThumbnail("1");     //이미지일경우 "1" 세팅
            file = fileService.insertFile(projectDto.getProjImgFile(),file);
            projectDto.getProject().setProjThumbnailImg(file.getFilePath());
        }
        //프로젝트 첨부파일 있을 경우 첨부
        if(!Util.isEmpty(projectDto.getProjAttFile())) {
            File file = new File();
            file.setProjId(project.getProjId());
            file.setProjThumbnail("0");     //첨부일경우 "0" 세팅
            file = fileService.insertFile(projectDto.getProjAttFile(),file);
            projectDto.getProject().setProjFile(file.getFilePath());
        }
        //프로젝트 해시태그 있을 경우 첨부
        if(!Util.isEmpty(projectDto.getProjHashtag())) {
            projectDto.getProjHashtag().setId(project.getProjId());
            projectDto.getProjHashtag().setLastRegistDt(new Date());
            projectDto.getProjHashtag().setLastRegistMngr(project.getProjMemId());
            hashtagService.insertAndUpdateHashtag(projectDto.getProjHashtag());
        }
        //프로필 있을 경우 프로필 첨부
        if (!Util.isEmpty(projectDto.getProfile())){
            profileRepository.findById(projectDto.getProfile().getProfileIdx()).ifPresentOrElse(temp -> {
                try {
                    profileService.insertAndUpdateProfile(projectDto);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, () ->{});
        }
        return projectDto.getProject();
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class}, timeout = 1000)
    public Project updateAndUpdateProject(ProjectDto projectDto) throws Exception {
        Project project = projectRepository.save(projectDto.getProject());
        //프로젝트 이미지 있을 경우 첨부
        if(!Util.isEmpty(projectDto.getProjImgFile())) {
            File file = new File();
            file.setProjId(project.getProjId());
            file.setProjThumbnail("1");     //이미지일경우 "1" 세팅
            file = fileService.insertFile(projectDto.getProjImgFile(),file);
            projectDto.getProject().setProjThumbnailImg(file.getFilePath());
        }
        //프로젝트 첨부파일 있을 경우 첨부
        if(!Util.isEmpty(projectDto.getProjAttFile())) {
            File file = new File();
            file.setProjId(project.getProjId());
            file.setProjThumbnail("0");     //첨부일경우 "0" 세팅
            file = fileService.insertFile(projectDto.getProjAttFile(),file);
            projectDto.getProject().setProjFile(file.getFilePath());
        }
        //프로젝트 해시태그 있을 경우 첨부
        if(!Util.isEmpty(projectDto.getProject().getProjKeyword())) {
            Hashtag hashtag = new Hashtag();
            hashtagRepository.findById(projectDto.getProject().getProjId()).ifPresentOrElse(temp -> {
                hashtag.setFrstRegistDt(temp.getFrstRegistDt());
                hashtag.setFrstRegistMngr(temp.getFrstRegistMngr());
                hashtag.setLastRegistDt(new Date());
                hashtag.setLastRegistMngr(projectDto.getProject().getProjId());
                hashtag.setHashType(temp.getHashType());
                hashtag.setId(temp.getId());
                hashtag.setHashNmList(Arrays.asList(projectDto.getProject().getProjKeyword().split(",")));
            }, ()->{
                hashtag.setFrstRegistDt(new Date());
                hashtag.setFrstRegistMngr(projectDto.getProject().getProjMemId());
                hashtag.setHashType(HashtagType.PROFILE.label);
                hashtag.setId(projectDto.getProject().getProjId());
                hashtag.setHashNmList(Arrays.asList(projectDto.getProject().getProjKeyword().split(",")));
            });

            hashtagService.insertAndUpdateHashtag(hashtag);
        } else {
            Hashtag hashtag = new Hashtag();
            hashtagRepository.findById(projectDto.getProject().getProjId()).ifPresent(temp -> {
                hashtag.setFrstRegistDt(temp.getFrstRegistDt());
                hashtag.setFrstRegistMngr(temp.getFrstRegistMngr());
                hashtag.setLastRegistDt(new Date());
                hashtag.setLastRegistMngr(projectDto.getProject().getProjId());
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
        //프로필 있을 경우 프로필 첨부
        if (!Util.isEmpty(projectDto.getProfile())){
            profileRepository.findById(projectDto.getProfile().getProfileIdx()).ifPresentOrElse(temp -> {
                try {
                    profileService.insertAndUpdateProfile(projectDto);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, () ->{});
        }
        return projectDto.getProject();
    }
}
