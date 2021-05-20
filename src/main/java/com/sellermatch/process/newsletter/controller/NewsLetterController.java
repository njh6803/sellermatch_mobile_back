package com.sellermatch.process.newsletter.controller;


import com.sellermatch.process.common.domain.CommonConstant;
import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.newsletter.domain.NewsLetter;
import com.sellermatch.process.newsletter.repository.NewsLetterRepository;
import com.sellermatch.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
public class NewsLetterController {

    @Autowired
    public NewsLetterRepository newsLetterRepository;

    @GetMapping("/newsLetter")
    public CommonDTO selectNewsLetter() {
        CommonDTO result = new CommonDTO();
        Pageable pageable = PageRequest.of(0,1);
        result.setContent(newsLetterRepository.findAll(pageable));
        return result;
    }

    @GetMapping("/newsLetter/list")
    public CommonDTO selectNewsLetterList(Pageable pageable) {
        CommonDTO result = new CommonDTO();
        result.setContent(newsLetterRepository.findAll(pageable));
        return result;
    }

    @PostMapping("/newsLetter")
    public CommonDTO insertNewsLetter(@RequestBody NewsLetter newsLetter) {
        CommonDTO result = new CommonDTO();
        if(Util.isEmail(newsLetter.getNewsLetterEmail())) {
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_FORMAT_104);
            return result;
        }
        result.setContent(newsLetterRepository.save(newsLetter));
        return result;
    }

    @PutMapping("/newsLetter")
    public CommonDTO updateNewsLetter(@RequestBody NewsLetter newsLetter) {
        CommonDTO result = new CommonDTO();
        newsLetterRepository.findById(newsLetter.getNewsLetterIdx()).ifPresentOrElse(temp ->{
            result.setContent(newsLetterRepository.save(newsLetter));
        }, () -> {});
        return result;
    }

/*    @DeleteMapping("/newsLetter")
    public CommonDTO deleteNewsLetter(NewsLetter newsLetter) {
        CommonDTO result = new CommonDTO();
        newsLetterRepository.findById(newsLetter.getNewsLetterIdx()).ifPresentOrElse(temp ->{
            newsLetterRepository.delete(newsLetter);
        }, () -> {});
        return result;
    }*/
}
