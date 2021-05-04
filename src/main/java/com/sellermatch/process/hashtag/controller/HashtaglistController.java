package com.sellermatch.process.hashtag.controller;

import com.sellermatch.process.hashtag.domain.Hashtag;
import com.sellermatch.process.hashtag.domain.Hashtaglist;
import com.sellermatch.process.hashtag.repository.HashtaglistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HashtaglistController {

    @Autowired
    public HashtaglistRepository hashtaglistRepository;

    @GetMapping("/hashtaglist")
    public List<Hashtaglist> selectHashtaglist() {
        return hashtaglistRepository.findAll();
    }
}
