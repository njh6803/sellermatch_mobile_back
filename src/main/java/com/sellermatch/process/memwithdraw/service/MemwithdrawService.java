package com.sellermatch.process.memwithdraw.service;

import com.sellermatch.process.member.domain.Member;
import com.sellermatch.process.member.repository.MemberRepository;
import com.sellermatch.process.memwithdraw.domain.MemWithdraw;
import com.sellermatch.process.memwithdraw.repository.MemwithdrawRepository;
import com.sellermatch.process.withdraw.domain.Withdraw;
import com.sellermatch.process.withdraw.repository.WithdrawRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemwithdrawService {

    @Autowired
    MemwithdrawRepository memwithdrawRepository;

    @Autowired
    WithdrawRepository withdrawRepository;

    @Autowired
    MemberRepository memberRepository;

    @Transactional
    public void insertMemwithdraw(Member member, Withdraw withdraw) throws Exception{
        memberRepository.save(member);

        MemWithdraw memWithdraw = new MemWithdraw();
        memWithdraw.setMemId(member.getMemId());
        memWithdraw.setMemPw(member.getMemPw());
        memWithdraw.setMemState(member.getMemState());

        memwithdrawRepository.save(memWithdraw);
        withdrawRepository.save(withdraw);
    }
}
