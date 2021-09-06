package com.sellermatch.process.member.service;

import com.sellermatch.process.member.domain.Member;
import com.sellermatch.process.member.repository.MemberRepository;
import com.sellermatch.process.profile.domain.Profile;
import com.sellermatch.process.profile.service.ProfileService;
import com.sellermatch.process.project.domain.ProjectDto;
import com.sellermatch.util.EncryptionUtils;
import com.sellermatch.util.KakaoHelp;
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
    private final KakaoHelp kakaoHelp;

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public Member insertMember(Member member) throws Exception{
        member.setMemRname("1");
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
        profile.setProfileVolume(0L);
        profile.setProfileSort(member.getMemSort());
        profile.setProfileRegDate(new Date());
        projectDto.setProfile(profile);
        profileService.insertAndUpdateProfile(projectDto);

        String subject = "셀러매치 가입을 환영합니다.";
        String type = "welcomeMail";

        mailUtil.sendMail(member.getMemId(), subject, member.getMemNick(), type);

        String parameters = "{\"message\": {\"to\": \""+member.getMemTel()+"\",\"from\": \"025150923\",\"text\": \"안녕하세요! '"+member.getMemNick()+"'님\\n셀러매치 회원가입을 환영합니다.\\n\\n판매자와 공급자를 잇는 공간,\\n셀러매치는 단순한 도매사이트가 아닙니다.\\n\\n'"+member.getMemNick()+"'님은\\n\\n- 돈되는, 팔만한 물건 찾기 OK\\n- 잘파는, 검증된 판매자 찾기 OK\\n- 조건에 맞는 공급제안 받기 OK\\n- 한 번 등록, 간편한 제안서 보내기 OK\\n\\n시간을 절약하세요\\n셀러매치가 함께합니다. (윙크)   \\n\",\"type\": \"ATA\",\"kakaoOptions\": {\"pfId\": \"KA01PF210708054305604abh2BH2e0wI\",\"title\":\"회원가입을 환영합니다\",\"templateId\": \"KA01TP210811034328964HdlR9ANhrt7\",\"buttons\": [{\"buttonName\": \"셀러매치 바로가기\",\"buttonType\": \"WL\",\"linkMo\": \"https://m.sellermatch.co.kr/\",\"linkPc\": \"https://www.sellermatch.co.kr/\"}]}}}";
        kakaoHelp.sendMessage(parameters);

        return member;
    }
}
