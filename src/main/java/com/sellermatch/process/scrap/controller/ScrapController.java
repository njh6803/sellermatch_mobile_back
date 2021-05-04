package com.sellermatch.process.scrap.controller;

import com.sellermatch.process.scrap.domain.Scrap;
import com.sellermatch.process.scrap.repository.ScrapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ScrapController {

    @Autowired
    private ScrapRepository scrapRepository;

    @GetMapping("/scrap")
    public List<Scrap> selectScrap() {
        return scrapRepository.findAll();
    }

}
