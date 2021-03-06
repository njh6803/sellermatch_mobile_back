package com.sellermatch.process.member.controller;

import com.sellermatch.process.common.domain.CommonConstant;
import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.member.domain.Member;
import com.sellermatch.process.member.repository.MemberRepository;
import com.sellermatch.process.member.service.MemberService;
import com.sellermatch.process.profile.repository.ProfileRepository;
import com.sellermatch.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api-v1")
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final JWTUtil jwtUtil;
    private final ProfileRepository profileRepository;

    @GetMapping("/member")
    public CommonDTO selectMember(String token) {
        CommonDTO result = new CommonDTO();
        if(token != null) {
            memberRepository.findTop1ByMemId(jwtUtil.getUserMemId(token)).ifPresentOrElse(temp -> {
                temp.setProfile(profileRepository.findTop1ByProfileMemId(temp.getMemId()));
                result.setContent(temp);
            }, () -> {
                Member emptyContent =  new Member();
                ControllerResultSet.errorCode(result, 0, emptyContent);
            });
        }
        // 토큰값 일치 확인
        return result;
    }

    @GetMapping("/member/list")
    public CommonDTO selectMemberList(Pageable pageable) {
        CommonDTO result = new CommonDTO();
        result.setContent(memberRepository.findAll(pageable));
        return result;
    }

    @PutMapping("/member/consent")
    public void selectMemberList(@RequestBody Member member) {
        memberRepository.findById(member.getMemIdx()).ifPresent(temp -> {
            temp.setMarketingConsent(member.getMarketingConsent());
            memberRepository.save(temp);
        });
    }

    @PutMapping("/member")
    public CommonDTO updateMember(@RequestBody Member member) throws Exception {
        CommonDTO result = new CommonDTO();

        //비밀번호 존재 시 체크
        if(!Util.isEmpty(member.getMemPw())){
            //비밀번호: NULL체크
            if(Util.isEmpty(member.getMemPw())){
                Member emptyContent =  new Member();
                ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_101, emptyContent);
                return result;
            }
            //비밀번호: 비밀번호 형식 체크(6자, 특문+영문+숫자)
            if(!Util.isPassword(member.getMemPw())) {
                Member emptyContent =  new Member();
                ControllerResultSet.errorCode(result, CommonConstant.ERROR_FORMAT_111, emptyContent);
                return result;
            }
            //비밀번호확인 : NULL체크
            if(Util.isEmpty(member.getMemPwChk())){
                Member emptyContent =  new Member();
                ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_213, emptyContent);
                return result;
            }
            //비밀번호: 비밀번호확인 일치 체크
            if(!member.getMemPwChk().equals(member.getMemPw())){
                Member emptyContent =  new Member();
                ControllerResultSet.errorCode(result, CommonConstant.ERROR_MISMATCH_112, emptyContent);
                return result;
            }
        }
        //이름 존재 시 체크
        if(!Util.isEmpty(member.getMemName())){
            //이름: 한글+영문만 가능
            if(!Util.isKorAndEng(member.getMemName())) {
                Member emptyContent =  new Member();
                ControllerResultSet.errorCode(result, CommonConstant.ERROR_FORMAT_118, emptyContent);
                return result;
            }
            //이름: 길이 체크 45자
            if(!Util.isLengthChk(member.getMemId(),0,45)) {
                Member emptyContent =  new Member();
                ControllerResultSet.errorCode(result, CommonConstant.ERROR_LENGTH_117, emptyContent);
                return result;
            }
        }
        //닉네임: NULL 체크
        if(Util.isEmpty(member.getMemNick())){
            Member emptyContent =  new Member();
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_113, emptyContent);
            return result;
        }
        //닉네임: 길이 체크 9자
        if(!Util.isLengthChk(member.getMemNick(),1,9)) {
            Member emptyContent =  new Member();
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_LENGTH_115, emptyContent);
            return result;
        }
        //닉네임: 중복 체크
        if(Util.isEmpty(memberRepository.findByMemNick(member.getMemNick()))){
            Member emptyContent =  new Member();
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_DUPLICATE_114, emptyContent);
            return result;
        }
        //전화번호: 전화번호 NULL 체크
        if(Util.isEmpty(member.getMemTel())){
            Member emptyContent =  new Member();
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_105, emptyContent);
            return result;
        }
        //전화번호: 전화번호 형식 체크
        if(!Util.isTel(member.getMemTel())){
            Member emptyContent =  new Member();
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_FORMAT_106, emptyContent);
            return result;
        }

        memberRepository.findById(member.getMemIdx()).ifPresent(temp -> {
            temp.setMemPw(EncryptionUtils.encryptMD5(member.getMemPw()));
            temp.setMemName(member.getMemName());
            temp.setMemCountry(member.getMemCountry());
            temp.setMemTel(member.getMemTel());
            temp.setMemNation(member.getMemNation());
            temp.setMarketingConsent(member.getMarketingConsent());
            temp.setAccountActiveConsent(member.getAccountActiveConsent());
            result.setContent(memberRepository.save(temp));
        });
        return result;
    }

/*    @DeleteMapping("/member")
    public CommonDTO deleteMember(Integer memIdx) {
        CommonDTO result = new CommonDTO();

        memberRepository.findById(memIdx).ifPresent(temp -> {
            memberRepository.delete(temp);
        });
        return result;
    }*/

}
