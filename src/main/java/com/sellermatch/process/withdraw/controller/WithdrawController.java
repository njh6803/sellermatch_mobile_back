package com.sellermatch.process.withdraw.controller;

import com.sellermatch.process.withdraw.domain.Withdraw;
import com.sellermatch.process.withdraw.repository.WithdrawRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WithdrawController {

    @Autowired
    public WithdrawRepository withdrawRepository;

    @GetMapping("/withdraw")
    public List<Withdraw> selectWithdraw() {
        return withdrawRepository.findAll();
    }
}
