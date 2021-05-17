package com.sellermatch.process.apply.controller;

import com.sellermatch.process.apply.domain.Apply;
import com.sellermatch.process.apply.repositiory.ApplyRepository;
import com.sellermatch.process.common.domain.CommonDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
public class ApplyController {

        @Autowired
        public ApplyRepository applyRepository;

        @GetMapping("/apply")
        public CommonDTO selectApply() {
                CommonDTO result = new CommonDTO();
                Pageable pageable = PageRequest.of(0,1);
                result.setContent(applyRepository.findAll(pageable));
                return result;
        }

        @GetMapping("/apply/list")
        public CommonDTO selectApplyList(Pageable pageable) {
                CommonDTO result = new CommonDTO();
                result.setContent(applyRepository.findAll(pageable));
                return result;
        }

        @PostMapping("/apply")
        public CommonDTO insertApply(Apply apply) {
                CommonDTO result = new CommonDTO();
                result.setContent(applyRepository.save(apply));
                return result;
        }

        @PutMapping("/apply")
        public CommonDTO updateApply(Apply apply) {
                CommonDTO result = new CommonDTO();
                applyRepository.findById(apply.getApplyIdx()).ifPresentOrElse(temp -> {
                        result.setContent(applyRepository.save(apply));
                }, () -> {});
                return result;
        }

        @DeleteMapping("/apply")
        public CommonDTO deleteApply(Apply apply) {
                CommonDTO result = new CommonDTO();
                applyRepository.findById(apply.getApplyIdx()).ifPresentOrElse(temp -> {
                        applyRepository.delete(apply);
                }, () -> {});
                return result;
        }
}
