package com.sellermatch.process.apply.controller;

import com.sellermatch.process.apply.domain.Apply;
import com.sellermatch.process.apply.repositiory.ApplyRepository;
import com.sellermatch.process.apply.repositiory.ApplyRepositoryCustom;
import com.sellermatch.process.common.domain.CommonConstant;
import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.member.repository.MemberRepository;
import com.sellermatch.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api-v1")
public class ApplyController {

        private final ApplyRepository applyRepository;
        private final ApplyRepositoryCustom applyRepositoryCustom;
        private final MemberRepository memberRepository;

        @GetMapping("/apply/{id}")
        public CommonDTO selectApply(@PathVariable Integer id) {
                CommonDTO result = new CommonDTO();
                applyRepository.findById(id).ifPresentOrElse(temp -> {
                        result.setContent(temp);
                } , () -> {
                        result.setContent("ERROR");
                        result.setStatus(CommonConstant.ERROR_998);
                        result.setContent(new Apply());
                });
                return result;
        }

        @GetMapping("/apply/list/{projId}")
        public CommonDTO selectApplyList(@PathVariable String projId, Pageable pageable) {
                CommonDTO result = new CommonDTO();
                Apply apply = new Apply();
                apply.setApplyProjId(projId);
                Page<Apply> applyList = applyRepositoryCustom.getApplyList(apply, pageable);
                result.setContent(applyList);
                return result;
        }

        @GetMapping("/apply/list")
        public CommonDTO selectApplyList(Pageable pageable) {
                CommonDTO result = new CommonDTO();
                result.setContent(applyRepository.findAll(pageable));
                return result;
        }

        @PostMapping("/apply/{memIdx}")
        public CommonDTO insertApply(@PathVariable Integer memIdx, Apply apply) {
                CommonDTO result = new CommonDTO();
                memberRepository.findById(memIdx).ifPresentOrElse(temp -> {
                        apply.setApplyId(Util.getUniqueId("A-", temp.getMemIdx()));
                        apply.setApplyRegDate(new Date());
                        apply.setApplyProfile(temp.getMemSort());
                        apply.setApplyMemId(temp.getMemId());
                }, ()->{});
                result.setContent(applyRepository.save(apply));
                return result;
        }

        @PutMapping("/apply")
        public CommonDTO updateApply(@RequestBody Apply apply) {
                CommonDTO result = new CommonDTO();
                applyRepository.findById(apply.getApplyIdx()).ifPresentOrElse(temp -> {
                        result.setContent(applyRepository.save(apply));
                }, () -> {});
                return result;
        }

/*        @DeleteMapping("/apply")
        public CommonDTO deleteApply(Apply apply) {
                CommonDTO result = new CommonDTO();
                applyRepository.findById(apply.getApplyIdx()).ifPresentOrElse(temp -> {
                        applyRepository.delete(apply);
                }, () -> {});
                return result;
        }*/
}
