package com.sellermatch.process.withdraw.controller;

import com.sellermatch.process.withdraw.domain.Withdraw;
import com.sellermatch.process.withdraw.repository.WithdrawRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WithdrawController {

    @Autowired
    public WithdrawRepository withdrawRepository;

    @GetMapping("/withdraw")
    public Page<Withdraw> selectWithdraw() {
        Pageable pageable = PageRequest.of(0,1);
        return withdrawRepository.findAll(pageable);
    }

    @GetMapping("/withdraw")
    public Page<Withdraw> selectWithdrawList(Pageable pageable) {
        return withdrawRepository.findAll(pageable);
    }

    @PostMapping("/withdraw")
    public Withdraw insertWithdraw(Withdraw withdraw) {
        return withdrawRepository.save(withdraw);
    }

    @PutMapping("/withdraw")
    public Withdraw updateWithdraw(Withdraw withdraw) {
        withdrawRepository.findById(withdraw.getWithdrawIdx()).ifPresentOrElse(temp -> {
            withdrawRepository.save(withdraw);
        }, () -> {});
        return withdraw;
    }

    @PutMapping("/withdraw")
    public Withdraw deleteWithdraw(Withdraw withdraw) {
        withdrawRepository.findById(withdraw.getWithdrawIdx()).ifPresentOrElse(temp -> {
            withdrawRepository.delete(withdraw);
        }, () -> {});
        return withdraw;
    }
}
