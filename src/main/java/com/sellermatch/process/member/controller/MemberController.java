package com.sellermatch.process.member.controller;

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
    public CommonDTO selectMemberList(Pageable pageable, String token) {
        CommonDTO result = new CommonDTO();
        result.setContent(memberRepository.findAll(pageable));
        return result;
    }

    @PostMapping("/member")
    public CommonDTO insertMember(Member member) throws Exception {
        CommonDTO result = new CommonDTO();
        result.setContent(memberService.insertMember(member));
        System.out.println(Util.isTel(member.getMemTel()));
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
