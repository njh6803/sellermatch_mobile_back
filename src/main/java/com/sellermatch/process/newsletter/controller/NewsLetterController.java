package com.sellermatch.process.newsletter.controller;


import com.sellermatch.process.common.domain.CommonConstant;
import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.newsletter.domain.NewsLetter;
import com.sellermatch.process.newsletter.repository.NewsLetterRepository;
import com.sellermatch.util.ControllerResultSet;
import com.sellermatch.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api-v1")
public class NewsLetterController {

    private final NewsLetterRepository newsLetterRepository;

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
        // 이름 NULL체크
        if (Util.isEmpty(newsLetter.getNewsLetterName())) {
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_116);
            return result;
        }
        // 이름 한글검사
        if (!Util.isKor(newsLetter.getNewsLetterName())) {
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_FORMAT_118);
            return result;
        }
        // 연락처(핸드폰) NULL체크
        if (Util.isEmpty(newsLetter.getNewsLetterPhone())) {
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_105);
            return result;
        }
        // 연락처형식 검사
        if (!Util.isCellPhone(newsLetter.getNewsLetterPhone())) {
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_FORMAT_106);
            return result;
        }
        // 이메일 NULL체크
        if (Util.isEmpty(newsLetter.getNewsLetterEmail())) {
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_233);
            return result;
        }
        // 이메일형식 검사
        if(!Util.isEmail(newsLetter.getNewsLetterEmail())) {
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_FORMAT_104);
            return result;
        }
        // 문의내용 NULL체크
        if (Util.isEmpty(newsLetter.getNewsLetterText())) {
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_229);
            return result;
        }
        newsLetter.setNewsLetterAgreeYN("Y");
        newsLetter.setNewsLetterRegDate(new Date());
        newsLetter.setNewsLetterEditDate(new Date());
        result.setContent(newsLetterRepository.save(newsLetter));
        return result;
    }

    @PutMapping("/newsletter")
    public CommonDTO updateNewsLetter(@RequestBody NewsLetter newsLetter) {
        CommonDTO result = new CommonDTO();
        newsLetterRepository.findById(newsLetter.getNewsLetterIdx()).ifPresentOrElse(temp ->{
            newsLetter.setNewsLetterEditDate(new Date());
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
