package com.sellermatch.process.apply.controller;

import com.sellermatch.process.apply.domain.Apply;
import com.sellermatch.process.apply.repositiory.ApplyRepository;
import com.sellermatch.process.apply.repositiory.ApplyRepositoryCustom;
import com.sellermatch.process.common.domain.CommonConstant;
import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.member.repository.MemberRepository;
import com.sellermatch.process.project.domain.Project;
import com.sellermatch.process.project.repository.ProjectRepositoryCustom;
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
        private final ProjectRepositoryCustom projectRepositoryCustom;

        @GetMapping("/apply/{id}")
        public CommonDTO selectApply(@PathVariable Integer id) {
                CommonDTO result = new CommonDTO();
                applyRepository.findById(id).ifPresentOrElse(temp -> {
                        result.setContent(temp);
                } , () -> {
                        result.setResult("ERROR");
                        result.setStatus(CommonConstant.ERROR_998);
                        result.setContent(new Apply());
                });
                return result;
        }

        @GetMapping("/recommend/project/{memIdx}")
        public CommonDTO selectRecommendProject(@PathVariable Integer memIdx, Pageable pageable) {
                CommonDTO result = new CommonDTO();
                Project project = new Project();

                //프로젝트 제안하기 마감거래는 조회되지 않도록 하기위한 플래그 "Y"면 마감거래 조회 안하기
                String recommandProjectFlag = "Y";

                memberRepository.findById(memIdx).ifPresentOrElse(temp -> {
                        project.setProjMemId(temp.getMemId());
                        project.setRecommandProjectFlag(recommandProjectFlag);
                        result.setContent(projectRepositoryCustom.findAllProject(project, pageable, null));
                }, ()->{
                        result.setResult("ERROR");
                        result.setStatus(CommonConstant.ERROR_NULL_216);
                        result.setContent(new Project());
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

        @PostMapping("/apply")
        public CommonDTO insertApply(@RequestBody Apply apply) {
                CommonDTO result = new CommonDTO();

                // 중복검사
                int count = applyRepository.countByApplyMemIdAndApplyProjIdAndApplyType(apply.getApplyMemId(), apply.getApplyProjId(), apply.getApplyType());
                if (count > 0) {
                        result.setResult("ERROR");
                        if (apply.getApplyType().equalsIgnoreCase("1")) {
                                result.setStatus(CommonConstant.ERROR_DUPLICATE_202);
                        }
                        if (apply.getApplyType().equalsIgnoreCase("2")) {
                                result.setStatus(CommonConstant.ERROR_DUPLICATE_207);
                        }
                        result.setContent(new Apply());

                        return result;
                }

                // 타입미일치
                if (apply.getProjSort().equalsIgnoreCase(apply.getMemSort())) {
                        result.setResult("ERROR");
                        if (apply.getApplyType().equalsIgnoreCase("1")) {
                                result.setStatus(CommonConstant.ERROR_TYPE_203);
                        }
                        if (apply.getApplyType().equalsIgnoreCase("2")) {
                                result.setStatus(CommonConstant.ERROR_TYPE_206);
                        }

                        result.setContent(new Apply());

                        return result;
                }

                // 자신의 게시물에 자신이 지원, 제안
                if (apply.getProjMemId().equalsIgnoreCase(apply.getApplyMemId())) {
                        result.setResult("ERROR");
                        result.setStatus(CommonConstant.ERROR_ACCESS_215);
                        result.setContent(new Apply());

                        return result;
                }

                memberRepository.findTop1ByMemId(apply.getApplyMemId()).ifPresentOrElse(temp -> {
                        apply.setApplyId(Util.getUniqueId("A-", temp.getMemIdx()));
                        apply.setApplyRegDate(new Date());
                        apply.setApplyProfile(temp.getMemSort());
                        apply.setApplyMemId(temp.getMemId());
                        result.setContent(applyRepository.save(apply));
                }, ()->{
                        result.setResult("ERROR");
                        result.setStatus(CommonConstant.ERROR_999);
                        result.setContent(new Apply());
                });

                // 지원
                if (apply.getApplyType().equalsIgnoreCase("1")) {

                }
                // 제안
                if (apply.getApplyType().equalsIgnoreCase("2")) {

                }
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
