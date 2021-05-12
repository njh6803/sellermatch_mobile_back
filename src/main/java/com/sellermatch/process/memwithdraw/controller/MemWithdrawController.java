package com.sellermatch.process.memwithdraw.controller;


import com.sellermatch.process.memwithdraw.domain.MemWithdraw;
import com.sellermatch.process.memwithdraw.repository.MemWithdrawRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
public class MemWithdrawController {

    @Autowired
    private MemWithdrawRepository memWithdrawRepository;

    @GetMapping("/memWithdraw")
    public Page<MemWithdraw> selectMemWithdraw() {
        Pageable pageable = PageRequest.of(0,1);
        return memWithdrawRepository.findAll(pageable);
    }

    @GetMapping("/memWithdraw/list")
    public Page<MemWithdraw> selectMemWithdrawList(Pageable pageable) {
        return memWithdrawRepository.findAll(pageable);
    }

    @PostMapping("/memWithdraw")
    public MemWithdraw insertMemWithdraw(MemWithdraw memWithdraw) {
        return memWithdrawRepository.save(memWithdraw);
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
