package com.sellermatch.process.mypage.controller;

import com.sellermatch.config.constant.ApplyType;
import com.sellermatch.config.constant.MemberType;
import com.sellermatch.process.apply.domain.Apply;
import com.sellermatch.process.apply.repositiory.ApplyRepository;
import com.sellermatch.process.apply.repositiory.ApplyRepositoryCustom;
import com.sellermatch.process.common.domain.CommonConstant;
import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.member.repository.MemberRepository;
import com.sellermatch.process.profile.domain.Profile;
import com.sellermatch.process.profile.repository.ProfileRepositoryCustom;
import com.sellermatch.process.project.domain.Project;
import com.sellermatch.process.project.repository.ProjectRepository;
import com.sellermatch.process.project.repository.ProjectRepositoryCustom;
import com.sellermatch.process.scrap.repository.ScrapRepository;
import com.sellermatch.util.ControllerResultSet;
import com.sellermatch.util.KakaoHelp;
import com.sellermatch.util.MailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api-v1")
public class MypageController {

    private final ProjectRepository projectRepository;
    private final ProjectRepositoryCustom projectRepositoryCustom;
    private final ProfileRepositoryCustom profileRepositoryCustom;
    private final ScrapRepository scrapRepository;
    private final ApplyRepository applyRepository;
    private final ApplyRepositoryCustom applyRepositoryCustom;
    private final MemberRepository memberRepository;
    private final MailUtil mailUtil;
    private final KakaoHelp kakaoHelp;

    @GetMapping("/myPage/myHome/{projMemId}")
    public CommonDTO selectProject(@PathVariable String projMemId) {
        CommonDTO result = new CommonDTO();
        Profile profile = profileRepositoryCustom.getMyProjectCount(projMemId);
        if (projectRepository.countByProjMemIdAndProjProdCerti(projMemId, "1") > 0) {
            profile.setProjProdCerti("1");
        }
        if (projectRepository.countByProjMemIdAndProjProfit(projMemId, "1") > 0) {
            profile.setProjProfit("1");
        }
        if (profile != null) {
            result.setContent(profile);
        } else {
            Profile emptyContent =  new Profile();
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_998, emptyContent);
        }
        return result;
    }

    @GetMapping("/myPage/registDelng/{memId}")
    public CommonDTO selectRegistDelng(@PathVariable String memId, Pageable pageable) {
        CommonDTO result = new CommonDTO();
        Page<Project> project = projectRepositoryCustom.getpRegistedProjectList(memId, pageable);
        if (project != null) {
            result.setContent(project);
        } else {
            Project emptyContent =  new Project();
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_998, emptyContent);
        }
        return result;
    }

    @GetMapping("/myPage/recommandList/{memId}/{memSort}")
    public CommonDTO selectRecommandList(@PathVariable String memId, @PathVariable String memSort, Pageable pageable) {
        CommonDTO result = new CommonDTO();
        Page<Project> project = null;
        if (memSort.equalsIgnoreCase(MemberType.PROVIDER.label)){
            project = projectRepositoryCustom.getRecommandListForPro(memId, pageable);
        }
        if (memSort.equalsIgnoreCase(MemberType.SELLER.label)) {
            project = projectRepositoryCustom.getRecommandListForSell(memId, pageable);
        }
        if (project != null) {
            result.setContent(project);
        } else {
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_998);
        }
        return result;
    }

    @GetMapping("/myPage/myApplyList/{memId}")
    public CommonDTO selectMyApplyList(@PathVariable String memId, Pageable pageable) {
        CommonDTO result = new CommonDTO();
        Page<Project> project = projectRepositoryCustom.getMyApplyList(memId, pageable);
        if (project != null) {
            result.setContent(project);
        } else {
            Project emptyContent = new Project();
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_998, emptyContent);
        }
        return result;
    }

    @GetMapping("/myPage/projectEndList/{memId}")
    public CommonDTO selectProjectEndList(@PathVariable String memId, Pageable pageable) {
        CommonDTO result = new CommonDTO();
        Page<Project> project = projectRepositoryCustom.getProjectEndList(memId, pageable);
        if (project != null) {
            result.setContent(project);
        } else {
            Project emptyContent = new Project();
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_998, emptyContent);
        }
        return result;
    }

    @GetMapping("/myPage/registDelng/project/{projId}")
    public CommonDTO selectRegistDelngProject(@PathVariable String projId, Pageable pageable) {
        CommonDTO result = new CommonDTO();
        projectRepository.findByProjId(projId).ifPresent(temp -> {

            Project project = projectRepositoryCustom.findProject(temp.getProjIdx(), 0);
            if (project != null) {
                result.setContent(project);
            } else {
                Project emptyContent = new Project();
                ControllerResultSet.errorCode(result, CommonConstant.ERROR_998, emptyContent);
            }
        });
        return result;
    }

    @GetMapping("/myPage/scrap/list/{memIdx}")
    public CommonDTO selectScrapList(@PathVariable Integer memIdx, Pageable pageable) {
        CommonDTO result = new CommonDTO();
        result.setContent(scrapRepository.findAllByMemIdx(pageable, memIdx));
        return result;
    }

    @PutMapping("/myPage/accept")
    public CommonDTO recommand(@RequestBody Apply apply) {
        CommonDTO result = new CommonDTO();
        applyRepository.findById(apply.getApplyIdx()).ifPresentOrElse(temp -> {
            applyRepository.updateApply(apply.getApplyIdx(), apply.getApplyProjState(), apply.getApplyType());
            result.setContent(new Apply());
            String applyTypeName = "";
            String projMemNick = "";
            String projTitle = "";
            String subject = "";
            String to = "";
            String nickName = "";
            String type = "";
            String memTel = "";
            String memSort = "";
            if (apply.getApplyType().equalsIgnoreCase(ApplyType.APPLY.label)) {
                Apply apply2 = applyRepositoryCustom.getAcceptedProjectOwner(temp);
                projTitle = apply2.getProjTitle();
                applyTypeName = "지원";
                subject = "SellerMatch 거래매칭 승인 결과 발송 메일";
                to = temp.getApplyMemId();
                nickName = memberRepository.findByMemId(apply2.getMemId()).getMemNick();
                projMemNick = memberRepository.findByMemId(to).getMemNick();
                memTel = memberRepository.findByMemId(to).getMemTel();
                memSort = apply2.getMemSort();

                type = "accept";

                /* 여기에 알림톡 추가 */
                if(memSort.equals(MemberType.PROVIDER.label)) {
                    String parameters = "{\"message\":{\"to\": \""+memTel+"\",\"from\": \"025150923\",\"text\": \"셀러매치에서 회원님이 지원한 거래처매칭 [공급지원 승인 알림]\\n\\n- 매칭명 : '"+projTitle+"' \\n- 승인자 : '"+nickName+"'\\n\\n[승인자 확인 방법]\\n\\n1. ‘마이페이지’ 클릭 (PC : 우측 상단 닉네임 클릭)\\n2. ‘지원한 거래’ 클릭\\n3. '"+nickName+"' 연락처 확인 후, 거래 시작\\n\\n* 해당 메시지는 거래처 매칭 게시글 등록자가 회원님의 거래 지원을 승인했을 경우 발송됩니다.\",\"type\": \"ATA\",\"kakaoOptions\": {\"pfId\": \"KA01PF210708054305604abh2BH2e0wI\",\"title\":\"공급지원 승인 알림\",\"templateId\": \"KA01TP210803043726531c2fOuTIkHJG\",\"buttons\": [{\"buttonName\": \"거래 승인 확인하러 가기\",\"buttonType\": \"WL\",\"linkMo\": \"https://m.sellermatch.co.kr/mypage\",\"linkPc\": \"https://sellermatch.co.kr/myPage/myHome\"}]}}}";
                    kakaoHelp.sendMessage(parameters);
                } else {
                    String parameters = "{\"message\":{\"to\": \""+memTel+"\",\"from\": \"025150923\",\"text\": \"셀러매치에서 회원님이 지원한 거래처매칭 [판매지원 승인 알림]\\n\\n- 매칭명 : '"+projTitle+"' \\n- 승인자 : '"+nickName+"'\\n\\n[승인자 확인 방법]\\n\\n1. ‘마이페이지’ 클릭 (PC : 우측 상단 닉네임 클릭)\\n2. ‘지원한 거래’ 클릭\\n3. '"+nickName+"' 연락처 확인 후, 거래 시작\\n\\n* 해당 메시지는 거래처 매칭 게시글 등록자가 회원님의 거래 지원을 승인했을 경우 발송됩니다.\",\"type\": \"ATA\",\"kakaoOptions\": {\"pfId\": \"KA01PF210708054305604abh2BH2e0wI\",\"title\":\"판매지원 승인 알림\",\"templateId\": \"KA01TP210803043229308SiT4wVEh6ND\",\"buttons\": [{\"buttonName\": \"거래 승인 확인하러 가기\",\"buttonType\": \"WL\",\"linkMo\": \"https://m.sellermatch.co.kr/mypage\",\"linkPc\": \"https://sellermatch.co.kr/myPage/myHome\"}]}}}";
                    kakaoHelp.sendMessage(parameters);
                }
                /* 여기에 알림톡 추가 끝*/
            }
            if (apply.getApplyType().equalsIgnoreCase(ApplyType.RECOMMEND.label)) {
                Apply apply2 = applyRepositoryCustom.getAcceptedRecommandOwner(temp);
                projTitle = apply2.getProjTitle();
                applyTypeName = "제안";
                subject = "SellerMatch 거래제안 승인 결과 발송 메일";
                to = apply2.getMemId();
                nickName = memberRepository.findByMemId(temp.getApplyMemId()).getMemNick();
                projMemNick = memberRepository.findByMemId(to).getMemNick();
                type = "recommandAccept";
                memTel = apply2.getMemTel();

                /* 여기에 알림톡 추가 */
                String parameters = "{\"message\":{\"to\": \""+memTel+"\",\"from\": \"025150923\",\"text\": \"셀러매치에서 회원님 공급제안 보고 승인한 [제안승인 알림]\\n\\n- 제안 상품 : '"+projTitle+"'\\n- 승인자 : '"+nickName+"'\\n\\n[승인자 확인 방법]\\n\\n1. ‘마이페이지’ 클릭 (PC : 우측 상단 닉네임 클릭)\\n2. ‘제안한 거래’ 클릭\\n3. '"+nickName+"' 연락처 확인 후, 거래 시작\\n\\n* 해당 메시지는 회원님의 공급제안 내용을 보고 제안을 받은 판매자가 제안 승인 할 경우 발송됩니다.\",\"type\": \"ATA\",\"kakaoOptions\": {\"pfId\": \"KA01PF210708054305604abh2BH2e0wI\",\"title\":\"제안승인 알림\",\"templateId\": \"KA01TP210803033949987K2XBjznIb3P\",\"buttons\": [{\"buttonName\": \"거래 승인 확인하러 가기\",\"buttonType\": \"WL\",\"linkMo\": \"https://m.sellermatch.co.kr/mypage\",\"linkPc\": \"https://sellermatch.co.kr/myPage/myHome\"}]}}}";
                kakaoHelp.sendMessage(parameters);
                /* 여기에 알림톡 추가 끝*/
            }



            mailUtil.sendMail(to, subject, nickName, type, applyTypeName, projTitle, projMemNick);
       }, () -> {
            Apply emptyContent = new Apply();
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_998, emptyContent);
        });
        return result;
    }
}
