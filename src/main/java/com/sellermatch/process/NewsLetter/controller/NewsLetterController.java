package com.sellermatch.process.NewsLetter.controller;


import com.sellermatch.process.NewsLetter.domain.NewsLetter;
import com.sellermatch.process.NewsLetter.repository.NewsLetterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

public class NewsLetterController {

    @Autowired
    public NewsLetterRepository newsLetterRepository;

    @GetMapping("/NewsLetter")
    public Page<NewsLetter> selectProfile() {
        Pageable pageable = PageRequest.of(0,1);
        return newsLetterRepository.findAll(pageable);
    }

    @GetMapping("/NewsLetter/list")
    public Page<NewsLetter> selectProfileList(Pageable pageable) {
        return newsLetterRepository.findAll(pageable);
    }

    @PostMapping("/NewsLetter")
    public NewsLetter insertProfile(NewsLetter newsLetter) {
        return newsLetterRepository.save(newsLetter);
    }

    @PutMapping("/NewsLetter")
    public NewsLetter updateProfile(NewsLetter newsLetter) {
        newsLetterRepository.findById(newsLetter.getNewsLetterIdx()).ifPresentOrElse(temp ->{
            newsLetterRepository.save(newsLetter);
        }, () -> {});
        return newsLetter;
    }

    @DeleteMapping("/NewsLetter")
    public NewsLetter deleteProfile(NewsLetter newsLetter) {
        newsLetterRepository.findById(newsLetter.getNewsLetterIdx()).ifPresentOrElse(temp ->{
            newsLetterRepository.delete(newsLetter);
        }, () -> {});
        return newsLetter;
    }
}
