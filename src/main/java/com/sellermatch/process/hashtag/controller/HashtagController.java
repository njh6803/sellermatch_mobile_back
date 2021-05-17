package com.sellermatch.process.hashtag.controller;

import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.hashtag.domain.Hashtag;
import com.sellermatch.process.hashtag.repository.HashtagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
public class HashtagController {

    @Autowired
    public HashtagRepository hashtagRepository;

    @GetMapping("/hashtag")
    public CommonDTO selectHashtag(){
        CommonDTO result = new CommonDTO();
        Pageable pageable = PageRequest.of(0,1);
        result.setContent(hashtagRepository.findAll(pageable));
        return result;
    }

    @GetMapping("/hashtag/list")
    public CommonDTO selectHashtagList(Pageable pageable){
        CommonDTO result = new CommonDTO();
        result.setContent(hashtagRepository.findAll(pageable));
        return result;
    }

    @PostMapping("/hashtag")
    public CommonDTO insertHashtag(Hashtag hashtag){
        CommonDTO result = new CommonDTO();
        result.setContent(hashtagRepository.save(hashtag));
        return result;
    }

    @PutMapping("/hashtag")
    public CommonDTO updateHashtag(Hashtag hashtag){
        CommonDTO result = new CommonDTO();
        hashtagRepository.findById(hashtag.getNo()).ifPresentOrElse(temp -> {
            result.setContent(hashtagRepository.save(hashtag));
        }, () -> {});
        return result;
    }

    @DeleteMapping("/hashtag")
    public CommonDTO deleteHashtag(Hashtag hashtag){
        CommonDTO result = new CommonDTO();
        hashtagRepository.findById(hashtag.getNo()).ifPresentOrElse(temp -> {
            hashtagRepository.delete(hashtag);
        }, () -> {});
        return result;
    }
}
