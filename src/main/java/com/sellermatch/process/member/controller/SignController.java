package com.sellermatch.process.member.controller;

import com.sellermatch.process.common.domain.CommonConstant;
import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.member.domain.Member;
import com.sellermatch.process.member.repository.MemberRepository;
import com.sellermatch.process.member.service.MemberService;
import com.sellermatch.util.EncryptionUtils;
import com.sellermatch.util.JWTUtil;
import com.sellermatch.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api-v1")
public class SignController {

    private final JWTUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @PostMapping("/signin")
    public CommonDTO signin(@RequestBody Member member) {
        CommonDTO result = new CommonDTO();
        if(Util.isEmpty(member.getMemSnsCh())) { // 일반회원 로그인
            memberRepository.findTop1ByMemId(member.getMemId()).ifPresentOrElse(validation -> {
                if(validation.getMemSnsCh().equalsIgnoreCase("01")) {
                    memberRepository.findByMemIdAndMemPw(member.getMemId(), EncryptionUtils.encryptMD5(member.getMemPw())).ifPresent(temp -> {
                        Map<String, Object> jwt = jwtUtil.createToken(member.getMemId());
                        result.setContent(jwt);
                    });
                } else {
                    Map<String, Object> jwt = new HashMap<>();
                    jwt.put("token", "");
                    jwt.put("expires", "");
                    result.setContent(jwt);
                    result.setResult("ERROR");
                    result.setStatus(CommonConstant.ERROR_MISMATCH_103);
                }
            }, () -> {
                Map<String, Object> jwt = new HashMap<>();
                jwt.put("token", "");
                jwt.put("expires", "");
                result.setContent(jwt);
                result.setResult("ERROR");
                result.setStatus(CommonConstant.ERROR_MISMATCH_102);
            });
        } else { // SNS 로그인
            memberRepository.findTop1ByMemIdAndMemSnsCh(member.getMemId(), member.getMemSnsCh()).ifPresentOrElse(temp -> {
                Map<String, Object> jwt = jwtUtil.createToken(member.getMemId());
                result.setContent(jwt);
            }, () -> {
                Map<String, Object> jwt = new HashMap<>();
                jwt.put("token", "");
                jwt.put("expires", "");
                result.setContent(jwt);
                result.setResult("ERROR");
                result.setStatus(CommonConstant.ERROR_MISMATCH_214);
            });
        }

        return result;
    }

    @PostMapping("/signup")
    public CommonDTO insertMember(
            HttpServletRequest request,
            @RequestBody Member member) throws Exception {
        CommonDTO result = new CommonDTO();

        member.setMemDate(new Date());

        //회원유형: NULL 체크
        if(Util.isEmpty(member.getMemSort())){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_NULL_107);
            return result;
        }
        //ID: NULL체크
        if(Util.isEmpty(member.getMemId())){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_NULL_100);
            return result;
        }
        //ID: 이메일형식 체크
        if(!Util.isEmail(member.getMemId())) {
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_FORMAT_104);
            return result;
        }
        //ID: 길이 체크 45자
        if(!Util.isLengthChk(member.getMemId(),0,45)) {
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_LENGTH_109);
            return result;
        }
        //ID: 중복체크
        if(!memberRepository.findTop1ByMemId(member.getMemId()).isEmpty()){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_DUPLICATE_108);
            return result;
        }

        //이메일 회원가입일 경우만 PW 체크
        if(member.getMemSnsCh().equalsIgnoreCase("01")){
            //비밀번호: NULL체크
            if(Util.isEmpty(member.getMemPw())){
                result.setResult(CommonConstant.ERROR);
                result.setStatus(CommonConstant.ERROR_NULL_101);
                return result;
            }
            //비밀번호: 비밀번호 형식 체크(6자, 특문+영문+숫자)
            if(!Util.isPassword(member.getMemPw())) {
                result.setResult(CommonConstant.ERROR);
                result.setStatus(CommonConstant.ERROR_FORMAT_111);
                return result;
            }
            //비밀번호확인 : NULL체크
            if(Util.isEmpty(member.getMemPwChk())){
                result.setResult(CommonConstant.ERROR);
                result.setStatus(CommonConstant.ERROR_NULL_213);
                return result;
            }
            //비밀번호: 비밀번호확인 일치 체크
            if(!member.getMemPwChk().equals(member.getMemPw())){
                result.setResult(CommonConstant.ERROR);
                result.setStatus(CommonConstant.ERROR_MISMATCH_102);
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
        if(!Util.isLengthChk(member.getMemNick(),1,100)) {
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_LENGTH_115);
            return result;
        }
        //닉네임: 중복 체크
        if(!memberRepository.findByMemNick(member.getMemNick()).isEmpty()){
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

        //IP 입력
        member.setMemIp(Util.getClientIP(request));
        result.setContent(memberService.insertMember(member));

        return result;
    }

}
