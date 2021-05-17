package com.sellermatch.process.reply.controller;

import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.reply.domain.Reply;
import com.sellermatch.process.reply.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
public class ReplyController {

    @Autowired
    public ReplyRepository replyRepository;

    @GetMapping("/reply")
    public CommonDTO selectReply() {
        CommonDTO result = new CommonDTO();
        Pageable pageable = PageRequest.of(0,1);
        result.setContent(replyRepository.findAll(pageable));
        return result;
    }

    @GetMapping("/reply/list")
    public CommonDTO selectReplyList(Pageable pageable) {
        CommonDTO result = new CommonDTO();
        result.setContent(replyRepository.findAll(pageable));
        return result;
    }

    @PostMapping("/reply")
    public CommonDTO insertReply(Reply reply) {
        CommonDTO result = new CommonDTO();
        result.setContent(replyRepository.save(reply));
        return result;
    }

    @PutMapping("/reply")
    public CommonDTO updateReply(Reply reply) {
        CommonDTO result = new CommonDTO();
        replyRepository.findById(reply.getReplyId()).ifPresentOrElse(temp -> {
            result.setContent(replyRepository.save(reply));
        }, () -> {});
        return result;
    }

    @DeleteMapping("/reply")
    public CommonDTO deleteReply(Reply reply) {
        CommonDTO result = new CommonDTO();
        replyRepository.findById(reply.getReplyId()).ifPresentOrElse(temp -> {
            replyRepository.save(reply);
        }, () -> {});
        return result;
    }
}
