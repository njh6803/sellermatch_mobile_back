package com.sellermatch.process.project.service;

import com.sellermatch.process.file.domain.File;
import com.sellermatch.process.file.service.FileService;
import com.sellermatch.process.hashtag.service.HashtagService;
import com.sellermatch.process.profile.service.ProfileService;
import com.sellermatch.process.project.domain.Project;
import com.sellermatch.process.project.domain.ProjectDto;
import com.sellermatch.process.project.repository.ProjectRepository;
import com.sellermatch.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectService {

    @Autowired
    FileService fileService;

    @Autowired
    HashtagService hashtagService;

    @Autowired
    ProfileService profileService;

    @Autowired
    private ProjectRepository projectRepository;

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public Project insertAndUpdateProject(ProjectDto projectDto) throws Exception {
        Project project = projectRepository.save(projectDto.getProject());

        //프로젝트 이미지 있을 경우 첨부
        if(!Util.isEmpty(projectDto.getProjImgFile())) {
            File file = new File();
            file.setProjId(project.getProjId());
            file.setProjThumbnail("1");     //이미지일경우 "1" 세팅
            fileService.insertFile(projectDto.getProjImgFile(),file);
        }
        //프로젝트 첨부파일 있을 경우 첨부
        if(!Util.isEmpty(projectDto.getProjAttFile())) {
            File file = new File();
            file.setProjId(project.getProjId());
            file.setProjThumbnail("0");     //첨부일경우 "0" 세팅
            fileService.insertFile(projectDto.getProjAttFile(),file);
        }
        //프로젝트 해시태그 있을 경우 첨부
        if(!Util.isEmpty(projectDto.getProjHashtag())) {
            projectDto.getProjHashtag().setId(project.getProjId());
            hashtagService.insertAndUpdateHashtag(projectDto.getProfileHashtag());
        }
        //프로필 있을 경우 프로필 첨부
        if(!Util.isEmpty(projectDto.getProfile())) {
            profileService.insertAndUpdateProfile(projectDto);
        }
        return projectDto.getProject();
    }

}
