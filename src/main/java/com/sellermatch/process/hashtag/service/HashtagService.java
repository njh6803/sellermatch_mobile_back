package com.sellermatch.process.hashtag.service;

import com.sellermatch.process.hashtag.domain.Hashtag;
import com.sellermatch.process.hashtag.domain.Hashtaglist;
import com.sellermatch.process.hashtag.repository.HashtagRepository;
import com.sellermatch.process.hashtag.repository.HashtaglistRepository;
import com.sellermatch.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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

    public void insertAndUpdateHashtag(Hashtag hashtag) throws Exception {
        AtomicInteger ai = new AtomicInteger();

        Hashtag eixstHashtag = hashtagRepository.findById(hashtag.getId());

        // 기존에 해시태그를 등록했는지
        if (!Util.isEmpty(eixstHashtag)) {
            hashtag.setNo(eixstHashtag.getNo());
        }

        hashtag.getHashNmList().forEach(s -> {
            int index = ai.getAndIncrement();
            hashtaglistRepository.findByHashNm(s).ifPresentOrElse(temp -> {
                tagSwitch(hashtag, index, temp);
            }, ()->{
                Hashtaglist hashtaglist = new Hashtaglist();
                hashtaglist.setHashNm(s);
                hashtaglist.setFrstRegistDt(new Date());
                hashtaglist.setFrstRegistMngr(hashtag.getFrstRegistMngr());
                hashtaglistRepository.save(hashtaglist);
                tagSwitch(hashtag, index, hashtaglist);
            });
        });
        hashtagRepository.save(hashtag);
    }

    private void tagSwitch(Hashtag hashtag, int index, Hashtaglist temp) {
        switch (index+1){
            case 1:
                hashtag.setHashTag1(temp.getHashId());
                break;
            case 2:
                hashtag.setHashTag2(temp.getHashId());
                break;
            case 3:
                hashtag.setHashTag3(temp.getHashId());
                break;
            case 4:
                hashtag.setHashTag4(temp.getHashId());
                break;
            case 5:
                hashtag.setHashTag5(temp.getHashId());
                break;
        }
    }
}
