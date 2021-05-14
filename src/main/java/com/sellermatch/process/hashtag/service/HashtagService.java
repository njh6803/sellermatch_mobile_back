package com.sellermatch.process.hashtag.service;

import com.sellermatch.process.hashtag.domain.Hashtag;
import com.sellermatch.process.hashtag.domain.Hashtaglist;
import com.sellermatch.process.hashtag.repository.HashtagRepository;
import com.sellermatch.process.hashtag.repository.HashtaglistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicInteger;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HashtagService {

    @Autowired
    HashtagRepository hashtagRepository;

    @Autowired
    HashtaglistRepository hashtaglistRepository;

    public void removeHashtag(Hashtag hashtag, Hashtaglist hashtaglist) throws Exception{
        hashtaglistRepository.delete(hashtaglist);
        hashtagRepository.delete(hashtag);
    }

    public void insertHashtag(Hashtag hashtag) throws Exception {
        AtomicInteger ai = new AtomicInteger();
        hashtag.getHashNmList().forEach(s -> {
            int index = ai.getAndIncrement();
            hashtaglistRepository.findByHashNm(s).ifPresentOrElse(temp -> {
                if (index == 0) {
                    hashtag.setHashTag1(temp.getHashId());
                } else if (index == 1){
                    hashtag.setHashTag2(temp.getHashId());
                } else if (index == 2){
                    hashtag.setHashTag3(temp.getHashId());
                } else if (index == 3){
                    hashtag.setHashTag4(temp.getHashId());
                } else {
                    hashtag.setHashTag5(temp.getHashId());
                }
            }, ()->{
                Hashtaglist hashtaglist = new Hashtaglist();
                hashtaglist.setHashNm(s);
                hashtaglistRepository.save(hashtaglist);
                if (index == 0){
                    hashtag.setHashTag1(hashtaglist.getHashId());
                } else if (index == 1){
                    hashtag.setHashTag2(hashtaglist.getHashId());
                } else if (index == 2){
                    hashtag.setHashTag3(hashtaglist.getHashId());
                } else if (index == 3){
                    hashtag.setHashTag4(hashtaglist.getHashId());
                } else {
                    hashtag.setHashTag5(hashtaglist.getHashId());
                }
            });
        });
        hashtagRepository.save(hashtag);
    }
}
