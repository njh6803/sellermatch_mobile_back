package com.sellermatch.process.scrap.controller;

import com.sellermatch.process.common.domain.CommonConstant;
import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.member.repository.MemberRepository;
import com.sellermatch.process.scrap.domain.Scrap;
import com.sellermatch.process.scrap.repository.ScrapRepository;
import com.sellermatch.process.scrap.repository.ScrapRepositoryCustom;
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

    @GetMapping("/scrap/{id}")
    public CommonDTO selectScrap(@PathVariable Integer id) {
        CommonDTO result = new CommonDTO();
        scrapRepository.findById(id).ifPresentOrElse(temp -> {
            result.setContent(temp);
        }, () -> {
            result.setResult("ERROR");
            result.setStatus(CommonConstant.ERROR_998);
            result.setContent(new Scrap());
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
        int count = scrapRepository.countByMemIdxAndProjIdx(scrap.getMemIdx(), scrap.getProjIdx());
        if (count > 0) {
            result.setResult("ERROR");
            result.setStatus(CommonConstant.ERROR_DUPLICATE_205);
            result.setContent(new Scrap());

            return result;
        }
        scrap.setFrstRegistDt(new Date());
        scrap.setLastRegistDt(new Date());
        memberRepository.findById(scrap.getMemIdx()).ifPresentOrElse(temp -> {
            scrap.setFrstRegistMngr(temp.getMemId());
            scrap.setLastRegistMngr(temp.getMemId());
        }, ()->{});

        result.setContent(scrapRepository.save(scrap));
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
            scrapRepository.delete(scrap);
        }, () -> {});
        return result;
    }
}
