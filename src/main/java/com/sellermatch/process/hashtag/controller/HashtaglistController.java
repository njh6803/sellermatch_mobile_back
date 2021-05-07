package com.sellermatch.process.hashtag.controller;

import com.sellermatch.process.hashtag.domain.Hashtaglist;
import com.sellermatch.process.hashtag.repository.HashtaglistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
public class HashtaglistController {

    @Autowired
    public HashtaglistRepository hashtaglistRepository;

    @GetMapping("/hashtaglist")
    public Page<Hashtaglist> selectHashtaglist() {
        Pageable pageable = PageRequest.of(0,1);
        return hashtaglistRepository.findAll(pageable);
    }

    @GetMapping("/hashtaglist/list")
    public Page<Hashtaglist> selectHashtaglistList(Pageable pageable) {
        return hashtaglistRepository.findAll(pageable);
    }

    @PostMapping("/hashtaglist")
    public Hashtaglist insertHashtaglist(Hashtaglist hashtaglist) {
        return hashtaglistRepository.save(hashtaglist);
    }

    @PutMapping("/hashtaglist")
    public Hashtaglist updateHashtaglist(Hashtaglist hashtaglist) {
        hashtaglistRepository.findById(hashtaglist.getHashId()).ifPresentOrElse(temp -> {
            hashtaglistRepository.save(hashtaglist);
        }, () -> {});
        return hashtaglist;
    }

    @DeleteMapping("/hashtaglist")
    public Hashtaglist deleteHashtaglist(Hashtaglist hashtaglist) {
        hashtaglistRepository.findById(hashtaglist.getHashId()).ifPresentOrElse(temp -> {
            hashtaglistRepository.delete(hashtaglist);
        }, () -> {});
        return hashtaglist;
    }
}
