package com.sellermatch.process.withdraw.controller;

import com.sellermatch.process.common.domain.CommonConstant;
import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.withdraw.domain.Withdraw;
import com.sellermatch.process.withdraw.repository.WithdrawRepository;
import com.sellermatch.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
public class WithdrawController {

    @Autowired
    public WithdrawRepository withdrawRepository;

    @GetMapping("/withdraw")
    public CommonDTO selectWithdraw() {
        CommonDTO result = new CommonDTO();
        Pageable pageable = PageRequest.of(0,1);
        result.setContent(withdrawRepository.findAll(pageable));
        return result;
    }

    @GetMapping("/withdraw/list")
    public CommonDTO selectWithdrawList(Pageable pageable) {
        CommonDTO result = new CommonDTO();
        result.setContent(withdrawRepository.findAll(pageable));
        return result;
    }

    @PostMapping("/withdraw")
    public CommonDTO insertWithdraw(Withdraw withdraw) {
        CommonDTO result = new CommonDTO();

        //탈퇴사유: NULL 체크
        if(Util.isEmpty(withdraw.getWithdrawReason())){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_NULL_145);
            return result;
        }
        //탈퇴인증코드: NULL 체크
        if(Util.isEmpty(withdraw.getWidthdrawAuthCode())){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_NULL_147);
            return result;
        }

        result.setContent(withdrawRepository.save(withdraw));
        return result;
    }

    @PutMapping("/withdraw")
    public CommonDTO updateWithdraw(Withdraw withdraw) {
        CommonDTO result = new CommonDTO();
        withdrawRepository.findById(withdraw.getWithdrawIdx()).ifPresentOrElse(temp -> {
            result.setContent(withdrawRepository.save(withdraw));
        }, () -> {});
        return result;
    }

    @DeleteMapping("/withdraw")
    public CommonDTO deleteWithdraw(Withdraw withdraw) {
        CommonDTO result = new CommonDTO();
        withdrawRepository.findById(withdraw.getWithdrawIdx()).ifPresentOrElse(temp -> {
            withdrawRepository.delete(withdraw);
        }, () -> {});
        return result;
    }
}
