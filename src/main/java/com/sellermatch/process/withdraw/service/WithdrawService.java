package com.sellermatch.process.withdraw.service;

import com.sellermatch.process.member.domain.Member;
import com.sellermatch.process.member.repository.MemberRepository;
import com.sellermatch.process.withdraw.domain.Withdraw;
import com.sellermatch.process.withdraw.repository.WithdrawRepository;
import com.sellermatch.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WithdrawService {

    @Autowired
    WithdrawRepository withdrawRepository;

    @Autowired
    MemberRepository memberRepository;

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public Withdraw insert(Withdraw withdraw) throws Exception {
        if(!Util.isEmpty(memberRepository.findByMemIdxAndWidthdrawAuthCode(withdraw.getMemIdx(),withdraw.getWidthdrawAuthCode()))) {
            withdrawRepository.save(withdraw);
            Member member = new Member();
            member.setMemIdx(withdraw.getMemIdx());
            member.setMemId(withdraw.getMemId());
            member.setWidthdrawAuthCode(withdraw.getWidthdrawAuthCode());
            member.setMemState("1");
            memberRepository.save(member);
        }
        return withdraw;
    }
}
