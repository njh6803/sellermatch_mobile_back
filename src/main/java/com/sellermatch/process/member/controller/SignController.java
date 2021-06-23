package com.sellermatch.process.member.controller;

import com.sellermatch.config.constant.SnsChType;
import com.sellermatch.process.common.domain.CommonConstant;
import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.member.domain.Member;
import com.sellermatch.process.member.repository.MemberRepository;
import com.sellermatch.process.member.service.MemberService;
import com.sellermatch.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api-v1")
public class SignController {

    private final JWTUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final MailUtil mailUtil;

    @PostMapping("/signin")
    public CommonDTO signin(@RequestBody Member member) {
        CommonDTO result = new CommonDTO();
        if(Util.isEmpty(member.getMemSnsCh())) { // 일반회원 로그인
            memberRepository.findTop1ByMemId(member.getMemId()).ifPresentOrElse(validation -> {
                if(validation.getMemSnsCh().equalsIgnoreCase(SnsChType.EMAIL.label)) {
                    memberRepository.findByMemIdAndMemPw(member.getMemId(), EncryptionUtils.encryptMD5(member.getMemPw())).ifPresentOrElse(temp -> {
                        Map<String, Object> jwt = jwtUtil.createToken(temp.getMemId());
                        result.setContent(jwt);
                    }, () -> {
                        Map<String, Object> jwt = new HashMap<>();
                        jwt.put("token", "");
                        jwt.put("expires", "");
                        result.setContent(jwt);
                        result.setResult("ERROR");
                        result.setStatus(CommonConstant.ERROR_MISMATCH_102);
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
                if (member.getMemSnsCh().equalsIgnoreCase(SnsChType.NAVER.label)){
                    Map<String, Object> jwt = new HashMap<>();
                    jwt.put("token", "");
                    jwt.put("expires", "");
                    result.setContent(jwt);
                    result.setResult("ERROR");
                    result.setStatus(CommonConstant.ERROR_ACCESS_223);
                } else {
                    Map<String, Object> jwt = new HashMap<>();
                    jwt.put("token", "");
                    jwt.put("expires", "");
                    result.setContent(jwt);
                    result.setResult("ERROR");
                    result.setStatus(CommonConstant.ERROR_MISMATCH_214);
                }
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
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_107);
            return result;
        }
        //ID: NULL체크
        if(Util.isEmpty(member.getMemId())){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_100);
            return result;
        }
        //ID: 이메일형식 체크
        if(!Util.isEmail(member.getMemId())) {
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_FORMAT_104);
            return result;
        }
        //ID: 길이 체크 45자
        if(!Util.isLengthChk(member.getMemId(),0,45)) {
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_LENGTH_109);
            return result;
        }
        //ID: 중복체크
        if(!memberRepository.findTop1ByMemId(member.getMemId()).isEmpty()){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_DUPLICATE_108);
            return result;
        }

        //이메일 회원가입일 경우만 PW 체크
        if(member.getMemSnsCh().equalsIgnoreCase(SnsChType.EMAIL.label)){
            //비밀번호: NULL체크
            if(Util.isEmpty(member.getMemPw())){
                ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_101);
                return result;
            }
            //비밀번호: 비밀번호 형식 체크(6자, 특문+영문+숫자)
            if(!Util.isPassword(member.getMemPw())) {
                ControllerResultSet.errorCode(result, CommonConstant.ERROR_FORMAT_111);
                return result;
            }
            //비밀번호확인 : NULL체크
            if(Util.isEmpty(member.getMemPwChk())){
                ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_213);
                return result;
            }
            //비밀번호: 비밀번호확인 일치 체크
            if(!member.getMemPwChk().equalsIgnoreCase(member.getMemPw())){
                ControllerResultSet.errorCode(result, CommonConstant.ERROR_MISMATCH_112);
                return result;
            }
        }
        //닉네임: NULL 체크
        if(Util.isEmpty(member.getMemNick())){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_113);
            return result;
        }
        //닉네임: 길이 체크 100자
        if(!Util.isLengthChk(member.getMemNick(),1,9)) {
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_LENGTH_115);
            return result;
        }
        //닉네임: 중복 체크
        if(!memberRepository.findByMemNick(member.getMemNick()).isEmpty()){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_DUPLICATE_114);
            return result;
        }
        //전화번호: 전화번호 NULL 체크
        if(Util.isEmpty(member.getMemTel())){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_105);
            return result;
        }
        //이용약관 동의 NULL 체크
        if(!Util.isTel(member.getTosConsent())){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_225);
            return result;
        }
        //개인정보처리방침 동의 NULL 체크
        if(!Util.isTel(member.getPrivacyConsent())){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_226);
            return result;
        }
        //14세 이상 동의 NULL 체크
        if(!Util.isTel(member.getAgeConsent())){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_227);
            return result;
        }
        //마케팅 수신동의 NULL 체크
        if(!Util.isTel(member.getMarketingConsent())){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_228);
            return result;
        }
        //계정활성상태유지 동의 NULL 체크
        if(!Util.isTel(member.getAccountActiveConsent())){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_229);
            return result;
        }

        //IP 입력
        member.setMemIp(Util.getClientIP(request));
        result.setContent(memberService.insertMember(member));

        return result;
    }

    @PostMapping("/signin/find/id")
    public CommonDTO findId(@RequestBody String memTel) {
        CommonDTO result = new CommonDTO();

        if (!Util.isTel(memTel)) {
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_FORMAT_106);
            return result;
        }

        if (Util.isEmpty(memTel)) {
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_105);
            return result;
        }

        List<String> idList = memberRepository.findId(memTel);
        if (!Util.isEmpty(idList)) {
            result.setContent(idList);
        } else {
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_221);
            return result;
        }
        return result;
    }

    @PostMapping("/signin/find/pw")
    public CommonDTO findPw(@RequestBody String memId) {
        CommonDTO result = new CommonDTO();

        //ID: NULL체크
        if(Util.isEmpty(memId)){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_100);
            return result;
        }
        //ID: 이메일형식 체크
        if(!Util.isEmail(memId)) {
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_FORMAT_104);
            return result;
        }
        //ID: 길이 체크 45자
        if(!Util.isLengthChk(memId,0,45)) {
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_LENGTH_109);
            return result;
        }

        memberRepository.findTop1ByMemId(memId).ifPresentOrElse(temp -> {
            if (!temp.getMemSnsCh().equalsIgnoreCase(SnsChType.EMAIL.label)) {
                ControllerResultSet.errorCode(result, CommonConstant.ERROR_ACCESS_222);
            } else {
                char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                        'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
                String tempPw = "";
                int idx = 0;
                for (int i = 0; i < 10; i++) {
                    idx = (int) (charSet.length * Math.random());
                    tempPw += charSet[idx];
                }
                memberRepository.changePw(EncryptionUtils.encryptMD5(tempPw), memId);
                String subject = "SellerMatch 비밀번호 찾기 메일";
                String type = "findPw";
                mailUtil.sendMail(memId, subject, type, tempPw);
            }
        }, ()->{
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_221);
        });
        return result;
    }
}
