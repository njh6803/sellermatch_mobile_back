package com.sellermatch.process.member.controller;

import com.sellermatch.process.member.domain.Member;
import com.sellermatch.process.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    @Autowired
    public MemberRepository memberRepository;

    @GetMapping("/member")
    public Page<Member> selectMember() {
        Pageable sortedByName = PageRequest.of(0, 3);
        return (Page<Member>) memberRepository.findAll(sortedByName);
    }

    @GetMapping("/member/list")
    public Page<Member> selectMemberList(Pageable pageable) {
//        Pageable sortedByName = PageRequest.of(0, 3);
        return memberRepository.findAll(pageable);
    }

    @PostMapping("/member")
    public Member insertMember(Member member) {
        return memberRepository.save(member);
    }

    @DeleteMapping("/member")
    public String deleteMember(Integer memIdx) {
        String result = "fail";
        memberRepository.findById(memIdx).ifPresent(temp -> {
            memberRepository.delete(temp);
        });
        return result;
    }

}
