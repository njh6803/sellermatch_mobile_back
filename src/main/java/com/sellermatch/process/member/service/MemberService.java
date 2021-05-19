package com.sellermatch.process.member.service;

import com.sellermatch.process.member.domain.Member;
import com.sellermatch.process.member.repository.MemberRepository;
import com.sellermatch.process.profile.domain.Profile;
import com.sellermatch.process.profile.service.ProfileService;
import com.sellermatch.process.project.domain.ProjectDto;
import com.sellermatch.util.EncryptionUtils;
import com.sellermatch.util.Util;
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

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public Member insertMember(Member member) throws Exception{
        member.setMemRname("0");
        member.setMemState("0");
        member.setMemClass("0");
        // 비밀번호 암호화
        member.setMemPw(EncryptionUtils.encryptMD5(member.getMemPw()));

        memberRepository.save(member);
        ProjectDto projectDto = new ProjectDto();
        Profile profile = new Profile();
        profile.setProfileId(Util.getUniqueId("PF-", Integer.parseInt(member.getMemSort())));
        profile.setProfileMemId(member.getMemId());
        profile.setProfileGrade("1");
        profile.setProfileChChk("0");
        profile.setProfileCareer("0");
        profile.setProfileSaleChk("0");
        profile.setProfileBizCerti("0");
        profile.setProfileState("1");
        profile.setProfileVolume(0);
        profile.setProfileSort(member.getMemSort());
        projectDto.setProfile(profile);
        profileService.insertAndUpdateProfile(projectDto);
        return member;
    }
}
