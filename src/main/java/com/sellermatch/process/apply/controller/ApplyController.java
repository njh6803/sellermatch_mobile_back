package com.sellermatch.process.apply.controller;

import com.sellermatch.config.constant.ApplyType;
import com.sellermatch.config.constant.MemberType;
import com.sellermatch.process.apply.domain.Apply;
import com.sellermatch.process.apply.repositiory.ApplyRepository;
import com.sellermatch.process.apply.repositiory.ApplyRepositoryCustom;
import com.sellermatch.process.common.domain.CommonConstant;
import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.member.domain.Member;
import com.sellermatch.process.member.repository.MemberRepository;
import com.sellermatch.util.ControllerResultSet;
import com.sellermatch.util.MailUtil;
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
        private final MailUtil mailUtil;

        @GetMapping("/apply/{id}")
        public CommonDTO selectApply(@PathVariable Integer id) {
                CommonDTO result = new CommonDTO();
                applyRepository.findById(id).ifPresentOrElse(temp -> {
                        result.setContent(temp);
                } , () -> {
                        Apply emptyContent =  new Apply();
                        ControllerResultSet.errorCode(result, CommonConstant.ERROR_998, emptyContent);
                });
                return result;
        }

        @GetMapping("/apply/list/{projId}")
        public CommonDTO selectApplyList(@PathVariable String projId, Pageable pageable) {
                CommonDTO result = new CommonDTO();
                Apply apply = new Apply();
                apply.setApplyProjId(projId);
                apply.setApplyType(ApplyType.APPLY.label);
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

        @PostMapping("/apply")
        public CommonDTO insertApply(@RequestBody Apply apply) throws Exception {
                CommonDTO result = new CommonDTO();
                Apply emptyContent =  new Apply();

                // 자신의 게시물에 자신이 지원, 제안
                if (apply.getProjMemId().equalsIgnoreCase(apply.getApplyMemId())) {
                        ControllerResultSet.errorCode(result, CommonConstant.ERROR_ACCESS_215, emptyContent);
                        return result;
                }

                // 중복검사
                int count = applyRepository.countByApplyMemIdAndApplyProjIdAndApplyType(apply.getApplyMemId(), apply.getApplyProjId(), apply.getApplyType());
                if (count > 0) {
                        if (apply.getApplyType().equalsIgnoreCase(MemberType.SELLER.label)) {
                                ControllerResultSet.errorCode(result, CommonConstant.ERROR_DUPLICATE_202, emptyContent);
                        }
                        if (apply.getApplyType().equalsIgnoreCase(MemberType.PROVIDER.label)) {
                                ControllerResultSet.errorCode(result, CommonConstant.ERROR_DUPLICATE_207, emptyContent);
                        }
                        return result;
                }

                // 타입미일치
                if (apply.getProjSort().equalsIgnoreCase(apply.getMemSort())) {
                        if (apply.getMemSort().equalsIgnoreCase(ApplyType.APPLY.label)) {
                                ControllerResultSet.errorCode(result, CommonConstant.ERROR_TYPE_203, emptyContent);
                        }
                        if (apply.getMemSort().equalsIgnoreCase(ApplyType.RECOMMEND.label)) {
                                ControllerResultSet.errorCode(result, CommonConstant.ERROR_TYPE_206, emptyContent);
                        }
                        return result;
                }

                memberRepository.findTop1ByMemId(apply.getApplyMemId()).ifPresentOrElse(temp -> {
                        apply.setApplyId(Util.getUniqueId("A-", temp.getMemIdx()));
                        apply.setApplyRegDate(new Date());
                        apply.setApplyProfile(temp.getMemSort());
                        apply.setApplyMemId(temp.getMemId());
                        result.setContent(applyRepository.save(apply));

                        String subject = "";
                        String type = "";
                        String applyTypeName = "";

                        // 지원
                        if (apply.getApplyType().equalsIgnoreCase(ApplyType.APPLY.label)) {
                                type = "apply";
                                subject = "셀러매치 지원알림";
                                mailUtil.sendMail(apply.getProjMemId(),subject, type, temp.getMemNick());
                        }
                        // 제안
                        if (apply.getApplyType().equalsIgnoreCase(ApplyType.RECOMMEND.label)) {
                                type = "recommand";
                                subject = "셀러매치 제안알림";
                                applyTypeName = "제안";
                                Member projMember = memberRepository.findByMemId(apply.getProjMemId());
                                mailUtil.sendMail(apply.getApplyMemId(),subject, projMember.getMemNick() , type, applyTypeName);

                        }
                }, ()->{
                        ControllerResultSet.errorCode(result, CommonConstant.ERROR_999, emptyContent);
                });
                return result;
        }

        @PutMapping("/apply")
        public CommonDTO updateApply(@RequestBody Apply apply) {
                CommonDTO result = new CommonDTO();
                applyRepository.findById(apply.getApplyIdx()).ifPresentOrElse(temp -> {
                        result.setContent(applyRepository.save(apply));
                }, () -> {
                        Apply emptyContent = new Apply();
                        ControllerResultSet.errorCode(result, CommonConstant.ERROR_999, emptyContent);
                });
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
