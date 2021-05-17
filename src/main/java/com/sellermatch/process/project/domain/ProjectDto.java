package com.sellermatch.process.project.domain;

import com.sellermatch.process.hashtag.domain.Hashtag;
import com.sellermatch.process.hashtag.domain.Hashtaglist;
import com.sellermatch.process.profile.domain.Profile;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProjectDto {
    private Profile profile;
    private MultipartFile profileImgFile;   //프로필 이미지 파일
    private Hashtag profileHashtag;         //프로필 해시태그
    private Project project;
    private MultipartFile projImgFile;    //프로젝트 이미지 파일
    private MultipartFile projAttFile;      //프로젝트 첨부 파일
    private Hashtag projHashtag;            //프로젝트 해시태그
    private Hashtaglist projHashtaglist;    //프로젝트 해시태그리스트
}
