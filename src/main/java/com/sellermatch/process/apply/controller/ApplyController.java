package com.sellermatch.process.apply.controller;

import com.sellermatch.process.apply.domain.Apply;
import com.sellermatch.process.apply.repositiory.ApplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
public class ApplyController {

        @Autowired
        public ApplyRepository applyRepository;

        @GetMapping("/apply")
        public Page<Apply> selectApply() {
                Pageable pageable = PageRequest.of(0,1);
            return applyRepository.findAll(pageable);
        }

        @GetMapping("/apply/list")
        public Page<Apply> selectApplyList(Pageable pageable) {
                return applyRepository.findAll(pageable);
        }

        @PostMapping("/apply")
        public Apply insertApply(Apply apply) {
                return applyRepository.save(apply);
        }

        @PutMapping("/apply")
        public Apply updateApply(Apply apply) {
                applyRepository.findById(apply.getApplyIdx()).ifPresentOrElse(temp -> {
                        applyRepository.save(apply);
                }, () -> {});
                return apply;
        }

        @DeleteMapping("/apply")
        public Apply deleteApply(Apply apply) {
                applyRepository.findById(apply.getApplyIdx()).ifPresentOrElse(temp -> {
                        applyRepository.delete(apply);
                }, () -> {});
                return apply;
        }
}
