package com.sellermatch.process.newsletter.controller;


import com.sellermatch.process.newsletter.domain.NewsLetter;
import com.sellermatch.process.newsletter.repository.NewsLetterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
public class NewsLetterController {

    @Autowired
    public NewsLetterRepository newsLetterRepository;

    @GetMapping("/newsLetter")
    public Page<NewsLetter> selectNewsLetter() {
        Pageable pageable = PageRequest.of(0,1);
        return newsLetterRepository.findAll(pageable);
    }

    @GetMapping("/newsLetter/list")
    public Page<NewsLetter> selectNewsLetterList(Pageable pageable) {
        return newsLetterRepository.findAll(pageable);
    }

    @PostMapping("/newsLetter")
    public NewsLetter insertNewsLetter(NewsLetter newsLetter) {
        return newsLetterRepository.save(newsLetter);
    }

    @PutMapping("/newsLetter")
    public NewsLetter updateNewsLetter(NewsLetter newsLetter) {
        newsLetterRepository.findById(newsLetter.getNewsLetterIdx()).ifPresentOrElse(temp ->{
            newsLetterRepository.save(newsLetter);
        }, () -> {});
        return newsLetter;
    }

    @DeleteMapping("/newsLetter")
    public NewsLetter deleteNewsLetter(NewsLetter newsLetter) {
        newsLetterRepository.findById(newsLetter.getNewsLetterIdx()).ifPresentOrElse(temp ->{
            newsLetterRepository.delete(newsLetter);
        }, () -> {});
        return newsLetter;
    }
}
