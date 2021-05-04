package com.sellermatch.process.apply.controller;

import com.sellermatch.process.apply.domain.Apply;
import com.sellermatch.process.apply.repositiory.ApplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ApplyController {

        @Autowired
        public ApplyRepository applyRepository;

        @GetMapping("/apply")
        public List<Apply> selectApply() {
            return applyRepository.findAll();
        }
}
