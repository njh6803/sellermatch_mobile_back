package com.sellermatch.process.reply.controller;

import com.sellermatch.process.reply.domain.Reply;
import com.sellermatch.process.reply.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReplyController {

    @Autowired
    public ReplyRepository replyRepository;

    @GetMapping("/reply")
    public List<Reply> selectReply() {
        return replyRepository.findAll();
    }
}
