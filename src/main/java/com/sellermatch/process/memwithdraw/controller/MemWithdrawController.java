package com.sellermatch.process.memwithdraw.controller;


import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.member.domain.Member;
import com.sellermatch.process.memwithdraw.domain.MemWithdraw;
import com.sellermatch.process.memwithdraw.repository.MemwithdrawRepository;
import com.sellermatch.process.memwithdraw.service.MemwithdrawService;
import com.sellermatch.process.withdraw.domain.Withdraw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
public class MemWithdrawController {

    @Autowired
    private MemwithdrawRepository memWithdrawRepository;

    @Autowired
    private MemwithdrawService memwithdrawService;

    @GetMapping("/memWithdraw")
    public CommonDTO selectMemWithdraw() {
        CommonDTO result = new CommonDTO();
        Pageable pageable = PageRequest.of(0,1);
        result.setContent(memWithdrawRepository.findAll(pageable));
        return result;
    }

    @GetMapping("/memWithdraw/list")
    public Page<MemWithdraw> selectMemWithdrawList(Pageable pageable) {
        return memWithdrawRepository.findAll(pageable);
    }

    @PostMapping("/memWithdraw")
    public void insertMemWithdraw(Member member, MemWithdraw memWithdraw, Withdraw withdraw) {
        try {
            memwithdrawService.insertMemwithdraw(member, withdraw);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PutMapping("/memWithdraw")
    public MemWithdraw updateMemWithdraw(MemWithdraw memWithdraw) {
        memWithdrawRepository.findById(memWithdraw.getMemWithdrawIdx()).ifPresentOrElse(temp ->{
            memWithdrawRepository.save(memWithdraw);
        }, () -> {});
        return memWithdraw;
    }

    @DeleteMapping("/memWithdraw")
    public MemWithdraw deleteMemWithdraw(MemWithdraw memWithdraw) {
        memWithdrawRepository.findById(memWithdraw.getMemWithdrawIdx()).ifPresentOrElse(temp ->{
            memWithdrawRepository.delete(memWithdraw);
        }, () -> {});
        return memWithdraw;
    }
}
