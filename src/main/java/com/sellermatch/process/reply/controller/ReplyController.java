package com.sellermatch.process.reply.controller;

import com.sellermatch.process.reply.domain.Reply;
import com.sellermatch.process.reply.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
public class ReplyController {

    @Autowired
    public ReplyRepository replyRepository;

    @GetMapping("/reply")
    public Page<Reply> selectReply() {
        Pageable pageable = PageRequest.of(0,1);
        return replyRepository.findAll(pageable);
    }

    @GetMapping("/reply/list")
    public Page<Reply> selectReplyList(Pageable pageable) {
        return replyRepository.findAll(pageable);
    }

    @PostMapping("/reply")
    public Reply insertReply(Reply reply) {
        return replyRepository.save(reply);
    }

    @PutMapping("/reply")
    public Reply updateReply(Reply reply) {
        replyRepository.findById(reply.getReplyId()).ifPresentOrElse(temp -> {
            replyRepository.save(reply);
        }, () -> {});
        return reply;
    }

    @DeleteMapping("/reply")
    public Reply deleteReply(Reply reply) {
        replyRepository.findById(reply.getReplyId()).ifPresentOrElse(temp -> {
            replyRepository.save(reply);
        }, () -> {});
        return reply;
    }
}
