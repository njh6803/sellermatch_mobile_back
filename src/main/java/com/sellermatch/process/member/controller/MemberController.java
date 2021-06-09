package com.sellermatch.process.member.controller;

import com.sellermatch.process.common.domain.CommonConstant;
import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.member.domain.Member;
import com.sellermatch.process.member.repository.MemberRepository;
import com.sellermatch.process.member.service.MemberService;
import com.sellermatch.process.profile.repository.ProfileRepository;
import com.sellermatch.util.EncryptionUtils;
import com.sellermatch.util.JWTUtil;
import com.sellermatch.util.Util;
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
                result.setResult("ERROR");
                result.setStatus(0);
                result.setContent(new Member());
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

    @PutMapping("/member")
    public CommonDTO updateMember(@RequestBody Member member) throws Exception {
        CommonDTO result = new CommonDTO();

        //비밀번호 존재 시 체크
        if(!Util.isEmpty(member.getMemPw())){
            //비밀번호: 비밀번호 형식 체크(6자, 특문+영문+숫자)
            if(!Util.isPassword(member.getMemPw())) {
                result.setResult(CommonConstant.ERROR);
                result.setStatus(CommonConstant.ERROR_FORMAT_104);
                return result;
            }
            //비밀번호: 비밀번호확인 일치 체크
            if(!member.getMemPwChk().equals(member.getMemPw())){
                result.setResult(CommonConstant.ERROR);
                result.setStatus(CommonConstant.ERROR_FORMAT_111);
                return result;
            }
        }
        //이름 존재 시 체크
        if(!Util.isEmpty(member.getMemName())){
            //이름: 한글+영문만 가능
            if(!Util.isKorAndEng(member.getMemName())) {
                result.setResult(CommonConstant.ERROR);
                result.setStatus(CommonConstant.ERROR_FORMAT_118);
                return result;
            }
            //이름: 길이 체크 45자
            if(!Util.isLengthChk(member.getMemId(),0,45)) {
                result.setResult(CommonConstant.ERROR);
                result.setStatus(CommonConstant.ERROR_LENGTH_117);
                return result;
            }
        }
        //닉네임: NULL 체크
        if(Util.isEmpty(member.getMemNick())){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_NULL_113);
            return result;
        }
        //닉네임: 길이 체크 100자
        if(!Util.isLengthChk(member.getMemNick(),0,100)) {
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_LENGTH_115);
            return result;
        }
        //닉네임: 중복 체크
        if(Util.isEmpty(memberRepository.findByMemNick(member.getMemNick()))){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_DUPLICATE_114);
            return result;
        }
        //전화번호: 전화번호 NULL 체크
        if(Util.isEmpty(member.getMemTel())){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_NULL_105);
            return result;
        }
        //전화번호: 전화번호 형식 체크
        if(!Util.isTel(member.getMemTel())){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_FORMAT_106);
            return result;
        }

        memberRepository.findById(member.getMemIdx()).ifPresent(temp -> {
            temp.setMemPw(EncryptionUtils.encryptMD5(member.getMemPw()));
            temp.setMemName(member.getMemName());
            temp.setMemCountry(member.getMemCountry());
            temp.setMemTel(member.getMemTel());
            temp.setMemNation(member.getMemNation());
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
