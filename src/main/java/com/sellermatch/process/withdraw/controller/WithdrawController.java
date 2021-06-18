package com.sellermatch.process.withdraw.controller;

import com.sellermatch.process.common.domain.CommonConstant;
import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.withdraw.domain.Withdraw;
import com.sellermatch.process.withdraw.repository.WithdrawRepository;
import com.sellermatch.process.withdraw.service.WithdrawService;
import com.sellermatch.util.ControllerResultSet;
import com.sellermatch.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
public class WithdrawController {

    private final WithdrawRepository withdrawRepository;
    private final WithdrawService withdrawService;


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
    public CommonDTO selectWithdrawList(Pageable pageable) {
        CommonDTO result = new CommonDTO();
        result.setContent(withdrawRepository.findAll(pageable));
        return result;
    }

    @PostMapping("/withdraw")
    public CommonDTO insertWithdraw(@RequestBody Withdraw withdraw) throws Exception {
        CommonDTO result = new CommonDTO();

        //탈퇴사유: NULL 체크
        if(Util.isEmpty(withdraw.getWithdrawReason())){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_145);
            return result;
        }

        //탈퇴사유내용: 길이 제한 (500자 이내)
        if(!Util.isLengthChk(withdraw.getWithdrawReasonText(),0,500)){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_LENGTH_146);
            return result;
        }

        withdrawService.insert(withdraw);

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
