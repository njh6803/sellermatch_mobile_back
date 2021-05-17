package com.sellermatch.process.member.controller;

import com.sellermatch.process.common.domain.CommonConstant;
import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.member.domain.Member;
import com.sellermatch.process.member.repository.MemberRepository;
import com.sellermatch.process.member.service.MemberService;
import com.sellermatch.util.JWTUtil;
import com.sellermatch.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MemberController {

    @Autowired
    public MemberRepository memberRepository;

    @Autowired
    public MemberService memberService;

    @Autowired
    public JWTUtil jwtUtil;

    @GetMapping("/member")
    public CommonDTO selectMember() {
        CommonDTO result = new CommonDTO();
        Pageable sortedByName = PageRequest.of(0, 3);
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_ADMIN");

        result.setContent(memberRepository.findAll(sortedByName));

        return result;
    }

    @GetMapping("/member/list")
    public CommonDTO selectMemberList(Pageable pageable, String token, String str) {
        CommonDTO result = new CommonDTO();
        result.setContent(memberRepository.findAll(pageable));
        return result;
    }

    @PostMapping("/member")
    public CommonDTO insertMember(Member member) throws Exception {
        CommonDTO result = new CommonDTO();

        //NULL 체크
        if(Util.isEmpty(member.getMemId())){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_NULL_100);
            return result;
        }
        if(member.getMemSnsCh() == "01"){   //이메일 회원가입일 경우만 PW 체크
            if(Util.isEmpty(member.getMemPw())){
                result.setResult(CommonConstant.ERROR);
                result.setStatus(CommonConstant.ERROR_NULL_101);
                return result;
            }
            if(Util.isPassword(member.getMemPw())) {
                result.setResult(CommonConstant.ERROR);
                result.setStatus(CommonConstant.ERROR_FORMAT_104);
                return result;
            }
            if(Util.isEmpty(member.getMemPwChk())){
                result.setResult(CommonConstant.ERROR);
                result.setStatus(CommonConstant.ERROR_NULL_101);
                return result;
            }
        }
        if(Util.isEmpty(member.getMemTel())){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_NULL_105);
            return result;
        }
        if(Util.isEmpty(member.getMemSort())){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_NULL_107);
            return result;
        }
        //FORMAT 체크
        if(Util.isEmail(member.getMemId())) {
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_FORMAT_104);
            return result;
        }
        if(Util.isTel(member.getMemTel())){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_FORMAT_106);
            return result;
        }

        result.setContent(memberService.insertMember(member));
        return result;
    }

    @DeleteMapping("/member")
    public CommonDTO deleteMember(Integer memIdx) {
        CommonDTO result = new CommonDTO();

        memberRepository.findById(memIdx).ifPresent(temp -> {
            memberRepository.delete(temp);
        });
        return result;
    }

}
