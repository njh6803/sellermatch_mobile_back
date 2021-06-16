package com.sellermatch.process.withdraw.service;

import com.sellermatch.process.member.repository.MemberRepository;
import com.sellermatch.process.withdraw.domain.Withdraw;
import com.sellermatch.process.withdraw.repository.WithdrawRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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
        memberRepository.findById(withdraw.getMemIdx()).ifPresent( temp -> {
            withdraw.setWithdrawDate(new Date());
            withdraw.setMemId(temp.getMemId());
            withdrawRepository.save(withdraw);
            String memState = "1";
            memberRepository.withdraw(memState, withdraw.getMemId());
        });
        return withdraw;
    }
}
