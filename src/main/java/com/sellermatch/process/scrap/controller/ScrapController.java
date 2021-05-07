package com.sellermatch.process.scrap.controller;

import com.sellermatch.process.scrap.domain.Scrap;
import com.sellermatch.process.scrap.repository.ScrapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
public class ScrapController {

    @Autowired
    private ScrapRepository scrapRepository;

    @GetMapping("/scrap")
    public Page<Scrap> selectScrap() {
        Pageable pageable = PageRequest.of(0,1);
        return scrapRepository.findAll(pageable);
    }

    @GetMapping("/scrap")
    public Page<Scrap> selectScrapList(Pageable pageable) {
        return scrapRepository.findAll(pageable);
    }

    @PostMapping("/scrap")
    public Scrap insertScrap(Scrap scrap) {
        return scrapRepository.save(scrap);
    }

    @PutMapping("/scrap")
    public Scrap updateScrap(Scrap scrap) {
        scrapRepository.findById(scrap.getScrapNo()).ifPresentOrElse(temp -> {
            scrapRepository.save(scrap);
        }, () -> {});
        return scrap;
    }

    @DeleteMapping("/scrap")
    public Scrap deleteScrap(Scrap scrap) {
        scrapRepository.findById(scrap.getScrapNo()).ifPresentOrElse(temp -> {
            scrapRepository.delete(scrap);
        }, () -> {});
        return scrap;
    }
}
