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
import com.sellermatch.process.project.repository.ProjectRepository;
import com.sellermatch.util.ControllerResultSet;
import com.sellermatch.util.KakaoHelp;
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
        private final ProjectRepository projectRepository;
        private final MailUtil mailUtil;
        private final KakaoHelp kakaoHelp;

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
        public CommonDTO insertApply(@RequestBody Apply apply) {
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
                        String to = "";
                        String nickName = "";
                        String projMemNick = "";
                        String memTel = "";

                        // 지원
                        if (apply.getApplyType().equalsIgnoreCase(ApplyType.APPLY.label)) {
                                String projTitle = projectRepository.findByProjId(apply.getApplyProjId()).get().getProjTitle();
                                type = "apply";
                                subject = "셀러매치 지원알림";
                                applyTypeName = "지원";
                                to = apply.getProjMemId();
                                nickName = memberRepository.findByMemId(apply.getApplyMemId()).getMemNick();
                                projMemNick = memberRepository.findByMemId(to).getMemNick();
                                memTel = memberRepository.findByMemId(to).getMemTel();

                                mailUtil.sendMail(to, subject, nickName, type, applyTypeName, projTitle, projMemNick);

                                /* 여기에 알림톡 추가 */
                                String parameters = "{\"message\": {\"to\": \""+memTel+"\",\"from\": \"025150923\",\"text\":\"셀러매치에서 회원님이 등록한 거래처매칭 [지원 알림]\\n\\n- 매칭명 : '"+projTitle+"'\\n- 지원자 : '"+nickName+"'\\n\\n[지원자 확인 방법]\\n\\n1. ‘마이페이지’ 클릭 (PC : 우측 상단 닉네임 클릭)\\n2. ‘등록한 거래’ 클릭\\n3. ‘관리하기’ 클릭\\n4. 지원자 승인/거절\\n5. '"+nickName+"' 연락처 확인 후, 거래 시작\\n\\n* 해당 메시지는 회원님이 거래처 매칭에 등록한 게시글에 거래지원이 발생할 경우 발송됩니다.\",\"type\": \"ATA\",\"kakaoOptions\": {\"pfId\": \"KA01PF210708054305604abh2BH2e0wI\",\"title\":\"지원 알림\",\"templateId\": \"KA01TP210803033505543hzVZo9suKam\",\"buttons\": [{\"buttonName\": \"거래 지원 확인하러 가기\",\"buttonType\": \"WL\",\"linkMo\": \"https://m.sellermatch.co.kr/mypage\",\"linkPc\": \"https://sellermatch.co.kr/myPage/myHome\"}]}}}";
                                kakaoHelp.sendMessage(parameters);
                                /* 여기에 알림톡 추가 끝*/
                        }
                        // 제안
                        if (apply.getApplyType().equalsIgnoreCase(ApplyType.RECOMMEND.label)) {
                                String projTitle = projectRepository.findByProjId(apply.getApplyProjId()).get().getProjTitle();
                                Member projMember = memberRepository.findByMemId(apply.getProjMemId());
                                type = "recommand";
                                subject = "셀러매치 제안알림";
                                applyTypeName = "제안";
                                to = apply.getApplyMemId();
                                nickName = projMember.getMemNick();
                                projMemNick = memberRepository.findByMemId(to).getMemNick();
                                memTel = memberRepository.findByMemId(to).getMemTel();
                                mailUtil.sendMail(to, subject,  nickName, type, applyTypeName, projMemNick);

                                /* 여기에 알림톡 추가 */
                                String parameters = "{\"message\":{\"to\": \""+memTel+"\",\"from\": \"025150923\",\"text\": \"셀러매치에서 회원님 프로필 보고 제안한 [공급 제안접수 알림]\\n\\n- 제안 상품 : '"+projTitle+"'\\n- 제안자 : '"+nickName+"'\\n\\n[제안내용 확인 방법]\\n\\n1. ‘마이페이지’ 클릭 (PC : 우측 상단 닉네임 클릭)\\n2. ‘제안 받은 거래’ 클릭\\n3. ‘관리하기’ 클릭 (제안 상품 내용 검토)\\n4. 제안 승인/거절\\n5. '"+nickName+"' 연락처 확인 후, 거래 시작\\n\\n* 해당 메시지는 회원님의 판매자 프로필 내용을 보고 제안자가 공급제안을 할 경우 발송됩니다.\",\"type\": \"ATA\",\"kakaoOptions\": {\"pfId\": \"KA01PF210708054305604abh2BH2e0wI\",\"title\":\"공급 제안접수 알림\",\"templateId\": \"KA01TP210803042755617wlJLjsb10bL\",\"buttons\": [{\"buttonName\": \"거래 제안 확인하러 가기\",\"buttonType\": \"WL\",\"linkMo\": \"https://m.sellermatch.co.kr/mypage\",\"linkPc\": \"https://sellermatch.co.kr/myPage/myHome\"}]}}}";
                                kakaoHelp.sendMessage(parameters);
                                /* 여기에 알림톡 추가 끝*/
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
