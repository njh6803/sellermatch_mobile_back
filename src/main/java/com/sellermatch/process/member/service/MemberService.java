package com.sellermatch.process.member.service;

import com.sellermatch.process.member.domain.Member;
import com.sellermatch.process.member.repository.MemberRepository;
import com.sellermatch.process.profile.domain.Profile;
import com.sellermatch.process.profile.service.ProfileService;
import com.sellermatch.process.project.domain.ProjectDto;
import com.sellermatch.util.EncryptionUtils;
import com.sellermatch.util.MailUtil;
import com.sellermatch.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ProfileService profileService;
    private final MailUtil mailUtil;

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public Member insertMember(Member member) throws Exception{
        member.setMemRname("0");
        member.setMemState("0");
        member.setMemClass("0");
        member.setMemDate(new Date());
        member.setSessionKey("none");
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
        profile.setProfileRegDate(new Date());
        projectDto.setProfile(profile);
        profileService.insertAndUpdateProfile(projectDto);

        String subject = "셀러매치 가입을 환영합니다.";
        String type = "welcomeMail";

        mailUtil.sendMail(member.getMemId(), subject, member.getMemNick(), type);

        return member;
    }
}
