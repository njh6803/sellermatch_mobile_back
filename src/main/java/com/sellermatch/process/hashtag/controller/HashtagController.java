package com.sellermatch.process.hashtag.controller;

import com.sellermatch.process.hashtag.domain.Hashtag;
import com.sellermatch.process.hashtag.repository.HashtagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class HashtagController {

    @Autowired
    public HashtagRepository hashtagRepository;

    @GetMapping("/hashtag")
    public Page<Hashtag> selectHashtag(){
        Pageable pageable = PageRequest.of(0,1);
        return hashtagRepository.findAll(pageable);
    }

    @GetMapping("/hashtag/list")
    public Page<Hashtag> selectHashtagList(Pageable pageable){
        return hashtagRepository.findAll(pageable);
    }

    @PostMapping("/hashtag")
    public Hashtag insertHashtag(Hashtag hashtag){
        return hashtagRepository.save(hashtag);
    }

    @PutMapping("/hashtag")
    public Hashtag updateHashtag(Hashtag hashtag){
        hashtagRepository.findById(hashtag.getNo()).ifPresentOrElse(temp -> {
            hashtagRepository.save(hashtag);
        }, () -> {});
        return hashtag;
    }

    @DeleteMapping("/hashtag")
    public Hashtag deleteHashtag(Hashtag hashtag){
        hashtagRepository.findById(hashtag.getNo()).ifPresentOrElse(temp -> {
            hashtagRepository.delete(hashtag);
        }, () -> {});
        return hashtag;
    }
}
