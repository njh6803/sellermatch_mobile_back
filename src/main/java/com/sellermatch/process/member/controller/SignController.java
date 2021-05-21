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
import org.springframework.web.bind.annotation.*;

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
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public CommonDTO signin(@RequestBody Member member) {
        CommonDTO result = new CommonDTO();
        memberRepository.findByMemIdAndMemPw(member.getMemId(), EncryptionUtils.encryptMD5(member.getMemPw())).ifPresentOrElse(temp -> {
            Map<String, Object> jwt = jwtUtil.createToken(member.getMemId());
            result.setContent(jwt);
        }, () -> {
            Map<String, Object> jwt = new HashMap<>();
            jwt.put("token", "");
            jwt.put("expires", "");
            result.setContent(jwt);
            result.setResult("ERROR");
            result.setStatus(0);
        });
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
        if(Util.isEmpty(memberRepository.findByMemId(member.getMemId()))){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_DUPLICATE_108);
            return result;
        }
        //이메일 회원가입일 경우만 PW 체크
        if(member.getMemSnsCh() == "01"){
            //비밀번호: NULL체크
            if(Util.isEmpty(member.getMemPw())){
                result.setResult(CommonConstant.ERROR);
                result.setStatus(CommonConstant.ERROR_NULL_101);
                return result;
            }
            //비밀번호: 비밀번호 형식 체크(6자, 특문+영문+숫자)
            if(Util.isPassword(member.getMemPw())) {
                result.setResult(CommonConstant.ERROR);
                result.setStatus(CommonConstant.ERROR_FORMAT_111);
                return result;
            }
            //비밀번호: 비밀번호확인 일치 체크
            if(!member.getMemPwChk().equals(member.getMemPw())){
                result.setResult(CommonConstant.ERROR);
                result.setStatus(CommonConstant.ERROR_FORMAT_111);
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
