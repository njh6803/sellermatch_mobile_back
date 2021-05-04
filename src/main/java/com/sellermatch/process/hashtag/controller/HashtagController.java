package com.sellermatch.process.hashtag.controller;

import com.sellermatch.process.hashtag.domain.Hashtag;
import com.sellermatch.process.hashtag.repository.HashtagRepository;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HashtagController {

    @Autowired
    public HashtagRepository hashtagRepository;

    @GetMapping("/hashtag")
    public List<Hashtag> selectHashtag(){
        return hashtagRepository.findAll();
    }
}
