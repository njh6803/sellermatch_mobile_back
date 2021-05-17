package com.sellermatch.process.memwithdraw.controller;


import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.member.domain.Member;
import com.sellermatch.process.memwithdraw.domain.MemWithdraw;
import com.sellermatch.process.memwithdraw.repository.MemwithdrawRepository;
import com.sellermatch.process.memwithdraw.service.MemwithdrawService;
import com.sellermatch.process.withdraw.domain.Withdraw;
import org.springframework.beans.factory.annotation.Autowired;
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
    public CommonDTO selectMemWithdrawList(Pageable pageable) {
        CommonDTO result = new CommonDTO();
        result.setContent(memWithdrawRepository.findAll(pageable));
        return result;
    }

    @PostMapping("/memWithdraw")
    public CommonDTO insertMemWithdraw(Member member, MemWithdraw memWithdraw, Withdraw withdraw) {
        CommonDTO result = new CommonDTO();

        try {
            result = memwithdrawService.insertMemwithdraw(member, withdraw);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @PutMapping("/memWithdraw")
    public CommonDTO updateMemWithdraw(MemWithdraw memWithdraw) {
        CommonDTO result = new CommonDTO();
        memWithdrawRepository.findById(memWithdraw.getMemWithdrawIdx()).ifPresentOrElse(temp ->{
            result.setContent(memWithdrawRepository.save(memWithdraw));
        }, () -> {});
        return result;
    }

    @DeleteMapping("/memWithdraw")
    public CommonDTO deleteMemWithdraw(MemWithdraw memWithdraw) {
        CommonDTO result = new CommonDTO();
        memWithdrawRepository.findById(memWithdraw.getMemWithdrawIdx()).ifPresentOrElse(temp ->{
            memWithdrawRepository.delete(memWithdraw);
        }, () -> {});
        return result;
    }
}
