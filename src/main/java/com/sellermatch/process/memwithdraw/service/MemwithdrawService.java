package com.sellermatch.process.memwithdraw.service;

import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.member.domain.Member;
import com.sellermatch.process.member.repository.MemberRepository;
import com.sellermatch.process.memwithdraw.repository.MemwithdrawRepository;
import com.sellermatch.process.withdraw.domain.Withdraw;
import com.sellermatch.process.withdraw.repository.WithdrawRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

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

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public CommonDTO insertMemwithdraw(Member member, Withdraw withdraw) throws Exception{
        CommonDTO result = new CommonDTO();
        Map<String, Object> data = new HashMap<>();

        withdraw.setMemIdx(member.getMemIdx());
        withdraw.setMemId(member.getMemId());

        data.put("member", memberRepository.save(member));
        data.put("withdraw", withdrawRepository.save(withdraw));
        result.setContent(data);
        return result;
    };
}
