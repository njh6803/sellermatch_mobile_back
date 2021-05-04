package com.sellermatch.process.member.controller;

import com.sellermatch.process.member.domain.Member;
import com.sellermatch.process.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MemberController {

    @Autowired
    public MemberRepository memberRepository;

    @GetMapping("/")
    public List<Member> selectMember() {
        return memberRepository.findAll();
    }

}
