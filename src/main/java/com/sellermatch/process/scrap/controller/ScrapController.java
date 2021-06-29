package com.sellermatch.process.scrap.controller;

import com.sellermatch.process.common.domain.CommonConstant;
import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.member.repository.MemberRepository;
import com.sellermatch.process.project.repository.ProjectRepositoryCustom;
import com.sellermatch.process.scrap.domain.Scrap;
import com.sellermatch.process.scrap.repository.ScrapRepository;
import com.sellermatch.process.scrap.repository.ScrapRepositoryCustom;
import com.sellermatch.util.ControllerResultSet;
import com.sellermatch.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api-v1")
public class ScrapController {

    private final ScrapRepository scrapRepository;
    private final MemberRepository memberRepository;
    private final ScrapRepositoryCustom scrapRepositoryCustom;
    private final ProjectRepositoryCustom projectRepositoryCustom;

    @GetMapping("/scrap/{id}")
    public CommonDTO selectScrap(@PathVariable Integer id) {
        CommonDTO result = new CommonDTO();
        scrapRepository.findById(id).ifPresentOrElse(temp -> {
            result.setContent(temp);
        }, () -> {
            Scrap emptyContent =  new Scrap();
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_998, emptyContent);
        });
        return result;
    }

    @GetMapping("/scrap/list/{memIdx}")
    public CommonDTO selectScrapList(@PathVariable Integer memIdx, Pageable pageable) {
        CommonDTO result = new CommonDTO();
        result.setContent(scrapRepositoryCustom.getScrapList(memIdx, pageable));
        return result;
    }

    @PostMapping("/scrap")
    public CommonDTO insertScrap(@RequestBody Scrap scrap) {
        CommonDTO result = new CommonDTO();
        Scrap emptyContent =  new Scrap();

        // 자신의 게시물에 자신이 스크랩
        if (scrap.getProjMemId().equalsIgnoreCase(scrap.getMemId())) {
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_ACCESS_215,emptyContent);

            return result;
        }

        int count = scrapRepository.countByMemIdxAndProjIdx(scrap.getMemIdx(), scrap.getProjIdx());
        if (count > 0) {
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_DUPLICATE_205, emptyContent);

            return result;
        }

        scrap.setFrstRegistDt(new Date());
        scrap.setLastRegistDt(new Date());
        memberRepository.findById(scrap.getMemIdx()).ifPresentOrElse(temp -> {
            scrap.setFrstRegistMngr(temp.getMemId());
            scrap.setLastRegistMngr(temp.getMemId());
        }, ()->{});

        scrapRepository.save(scrap);
        if (Util.isEmpty(scrap.getScrapNo())) {
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_998);
            return result;
        } else {
            result.setContent(projectRepositoryCustom.findProject(scrap.getProjIdx(), scrap.getMemIdx()));
        }
        return result;
    }

    @PutMapping("/scrap")
    public CommonDTO updateScrap(@RequestBody Scrap scrap) {
        CommonDTO result = new CommonDTO();
        scrapRepository.findById(scrap.getScrapNo()).ifPresentOrElse(temp -> {
            result.setContent(scrapRepository.save(scrap));
        }, () -> {});
        return result;
    }

    @DeleteMapping("/scrap")
    public CommonDTO deleteScrap(Scrap scrap) {
        CommonDTO result = new CommonDTO();
        scrapRepository.findByMemIdxAndProjIdx(scrap.getMemIdx(), scrap.getProjIdx()).ifPresentOrElse(temp -> {
            scrapRepository.delete(temp);
            result.setContent(projectRepositoryCustom.findProject(scrap.getProjIdx(), scrap.getMemIdx()));
        }, () -> {});
        return result;
    }
}
