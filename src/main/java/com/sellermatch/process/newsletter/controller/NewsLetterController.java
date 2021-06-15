package com.sellermatch.process.newsletter.controller;


import com.sellermatch.process.common.domain.CommonConstant;
import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.newsletter.domain.NewsLetter;
import com.sellermatch.process.newsletter.repository.NewsLetterRepository;
import com.sellermatch.util.ControllerResultSet;
import com.sellermatch.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
public class NewsLetterController {

    @Autowired
    public NewsLetterRepository newsLetterRepository;

    @GetMapping("/newsletter/{id}")
    public CommonDTO selectNewsLetter(@PathVariable Integer id) {
        CommonDTO result = new CommonDTO();
        newsLetterRepository.findById(id).ifPresentOrElse(temp -> {
            result.setContent(temp);
        }, () -> {
            NewsLetter emptyContent =  new NewsLetter();
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_998, emptyContent);
        });
        return result;
    }

    @GetMapping("/newsletter/list")
    public CommonDTO selectNewsLetterList(Pageable pageable) {
        CommonDTO result = new CommonDTO();
        result.setContent(newsLetterRepository.findAll(pageable));
        return result;
    }

    @PostMapping("/newsletter")
    public CommonDTO insertNewsLetter(@RequestBody NewsLetter newsLetter) {
        CommonDTO result = new CommonDTO();
        if(Util.isEmail(newsLetter.getNewsLetterEmail())) {
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_FORMAT_104);
            return result;
        }
        result.setContent(newsLetterRepository.save(newsLetter));
        return result;
    }

    @PutMapping("/newsletter")
    public CommonDTO updateNewsLetter(@RequestBody NewsLetter newsLetter) {
        CommonDTO result = new CommonDTO();
        newsLetterRepository.findById(newsLetter.getNewsLetterIdx()).ifPresentOrElse(temp ->{
            result.setContent(newsLetterRepository.save(newsLetter));
        }, () -> {});
        return result;
    }

/*    @DeleteMapping("/newsletter")
    public CommonDTO deleteNewsLetter(NewsLetter newsLetter) {
        CommonDTO result = new CommonDTO();
        newsLetterRepository.findById(newsLetter.getNewsLetterIdx()).ifPresentOrElse(temp ->{
            newsLetterRepository.delete(newsLetter);
        }, () -> {});
        return result;
    }*/
}
