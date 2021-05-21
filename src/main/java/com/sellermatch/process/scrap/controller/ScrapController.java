package com.sellermatch.process.scrap.controller;

import com.sellermatch.process.common.domain.CommonConstant;
import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.scrap.domain.Scrap;
import com.sellermatch.process.scrap.repository.ScrapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
public class ScrapController {

    @Autowired
    private ScrapRepository scrapRepository;

    @GetMapping("/scrap/{ip}")
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

    @GetMapping("/scrap/list")
    public CommonDTO selectScrapList(Pageable pageable) {
        CommonDTO result = new CommonDTO();
        result.setContent(scrapRepository.findAll(pageable));
        return result;
    }

    @PostMapping("/scrap")
    public CommonDTO insertScrap(@RequestBody Scrap scrap) {
        CommonDTO result = new CommonDTO();
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

/*    @DeleteMapping("/scrap")
    public CommonDTO deleteScrap(Scrap scrap) {
        CommonDTO result = new CommonDTO();
        scrapRepository.findById(scrap.getScrapNo()).ifPresentOrElse(temp -> {
            scrapRepository.delete(scrap);
        }, () -> {});
        return result;
    }*/
}
