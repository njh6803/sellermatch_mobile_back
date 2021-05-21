package com.sellermatch.process.hashtag.controller;

import com.sellermatch.process.common.domain.CommonConstant;
import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.hashtag.domain.Hashtaglist;
import com.sellermatch.process.hashtag.repository.HashtaglistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
public class HashtaglistController {

    @Autowired
    public HashtaglistRepository hashtaglistRepository;

    @GetMapping("/hashtaglist/{id}")
    public CommonDTO selectHashtaglist(@PathVariable Integer id) {
        CommonDTO result = new CommonDTO();
        hashtaglistRepository.findById(id).ifPresentOrElse(temp -> {
            result.setContent(temp);
        }, () -> {
            result.setResult("ERROR");
            result.setStatus(CommonConstant.ERROR_998);
            result.setContent(new Hashtaglist());
        });
        return result;
    }

    @GetMapping("/hashtaglist/list")
    public CommonDTO selectHashtaglistList(Pageable pageable) {
        CommonDTO result = new CommonDTO();
        result.setContent(hashtaglistRepository.findAll(pageable));
        return result;
    }

    @PostMapping("/hashtaglist")
    public CommonDTO insertHashtaglist(@RequestBody Hashtaglist hashtaglist) {
        CommonDTO result = new CommonDTO();
        result.setContent(hashtaglistRepository.save(hashtaglist));
        return result;
    }

    @PutMapping("/hashtaglist")
    public CommonDTO updateHashtaglist(@RequestBody Hashtaglist hashtaglist) {
        CommonDTO result = new CommonDTO();
        hashtaglistRepository.findById(hashtaglist.getHashId()).ifPresentOrElse(temp -> {
            result.setContent(hashtaglistRepository.save(hashtaglist));
        }, () -> {});
        return result;
    }

/*    @DeleteMapping("/hashtaglist")
    public Hashtaglist deleteHashtaglist(Hashtaglist hashtaglist) {
        hashtaglistRepository.findById(hashtaglist.getHashId()).ifPresentOrElse(temp -> {
            hashtaglistRepository.delete(hashtaglist);
        }, () -> {});
        return hashtaglist;
    }*/
}
