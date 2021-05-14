package com.sellermatch.process.member.service;

import com.sellermatch.process.member.domain.Member;
import com.sellermatch.process.member.repository.MemberRepository;
import com.sellermatch.process.profile.domain.Profile;
import com.sellermatch.process.profile.service.ProfileService;
import com.sellermatch.process.project.domain.ProjectDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ProfileService profileService;

    @Transactional
    public void insertMember(Member member) throws Exception{
        memberRepository.save(member);
        ProjectDto projectDto = new ProjectDto();
        Profile profile = new Profile();
        projectDto.setProfile(profile);
        profileService.insertProfile(projectDto);
    }
}
