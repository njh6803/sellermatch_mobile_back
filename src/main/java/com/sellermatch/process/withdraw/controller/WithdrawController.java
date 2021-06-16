package com.sellermatch.process.withdraw.controller;

import com.sellermatch.process.common.domain.CommonConstant;
import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.withdraw.domain.Withdraw;
import com.sellermatch.process.withdraw.repository.WithdrawRepository;
import com.sellermatch.util.ControllerResultSet;
import com.sellermatch.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
public class WithdrawController {

    @Autowired
    public WithdrawRepository withdrawRepository;

    @GetMapping("/withdraw/{id}")
    public CommonDTO selectWithdraw(@PathVariable Integer id) {
        CommonDTO result = new CommonDTO();
        withdrawRepository.findById(id).ifPresentOrElse(temp -> {
            result.setContent(temp);
        }, () -> {
            Withdraw emptyContent =  new Withdraw();
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_998, emptyContent);
        });
        return result;
    }

    @GetMapping("/withdraw/list")
    public CommonDTO selectWithdrawList(Pageable pageable) throws Exception {
        CommonDTO result = new CommonDTO();
        result.setContent(withdrawRepository.findAll(pageable));
        return result;
    }

    @PostMapping("/withdraw")
    public CommonDTO insertWithdraw(@RequestBody Withdraw withdraw) {
        CommonDTO result = new CommonDTO();

        //탈퇴사유: NULL 체크
        if(Util.isEmpty(withdraw.getWithdrawReason())){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_145);
            return result;
        }
        //탈퇴인증코드: NULL 체크
        if(Util.isEmpty(withdraw.getWidthdrawAuthCode())){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_147);
            return result;
        }

        result.setContent(withdrawRepository.save(withdraw));
        return result;
    }

    @PutMapping("/withdraw")
    public CommonDTO updateWithdraw(@RequestBody Withdraw withdraw) {
        CommonDTO result = new CommonDTO();
        withdrawRepository.findById(withdraw.getWithdrawIdx()).ifPresentOrElse(temp -> {
            result.setContent(withdrawRepository.save(withdraw));
        }, () -> {});
        return result;
    }

/*    @DeleteMapping("/withdraw")
    public CommonDTO deleteWithdraw(Withdraw withdraw) {
        CommonDTO result = new CommonDTO();
        withdrawRepository.findById(withdraw.getWithdrawIdx()).ifPresentOrElse(temp -> {
            withdrawRepository.delete(withdraw);
        }, () -> {});
        return result;
    }*/
}
