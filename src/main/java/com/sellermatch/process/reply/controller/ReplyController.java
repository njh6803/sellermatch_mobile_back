package com.sellermatch.process.reply.controller;

import com.sellermatch.process.common.domain.CommonConstant;
import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.reply.domain.Reply;
import com.sellermatch.process.reply.repository.ReplyRepository;
import com.sellermatch.process.reply.repository.ReplyRepositoryCustom;
import com.sellermatch.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api-v1")
public class ReplyController {

    private final ReplyRepository replyRepository;
    private final ReplyRepositoryCustom replyRepositoryCustom;

    @GetMapping("/reply/{id}")
    public CommonDTO selectReply(@PathVariable Integer id) {
        CommonDTO result = new CommonDTO();

        replyRepository.findById(id).ifPresentOrElse(temp -> {
            result.setContent(temp);
        }, () -> {
            result.setResult("ERROR");
            result.setStatus(CommonConstant.ERROR_998);
            result.setContent(new Reply());
        });
        return result;
    }

    @GetMapping("/reply/list/{projId}/{boardId}")
    public CommonDTO selectReplyList(@PathVariable String projId, @PathVariable String boardId, Pageable pageable) {
        CommonDTO result = new CommonDTO();
        Reply reply = new Reply();
        if (!Util.isEmpty(projId)) {
            reply.setReplyProjId(projId);
        }
        if (!Util.isEmpty(boardId)) {
            reply.setReplyBoardId(boardId);
        }

        Page<Reply> replyList = replyRepositoryCustom.getReplyList(reply, pageable);
        result.setContent(replyList);
        return result;
    }

    @GetMapping("/reply/list")
    public CommonDTO selectReplyList(Pageable pageable) {
        CommonDTO result = new CommonDTO();
        result.setContent(replyRepository.findAll(pageable));
        return result;
    }

    @PostMapping("/reply")
    public CommonDTO insertReply(@RequestBody Reply reply) {
        CommonDTO result = new CommonDTO();
        //댓글내용: NULL 체크
        if(Util.isEmpty(reply.getReplyContents())){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_NULL_149);
            return result;
        }
        //댓글내용: 길이 체크 (100자)
        if(Util.isLengthChk(reply.getReplyContents(),0,100)){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_LENGTH_151);
            return result;
        }

        result.setContent(replyRepository.save(reply));
        return result;
    }

    @PutMapping("/reply")
    public CommonDTO updateReply(@RequestBody Reply reply) {
        CommonDTO result = new CommonDTO();
        replyRepository.findById(reply.getReplyId()).ifPresentOrElse(temp -> {
            result.setContent(replyRepository.save(reply));
        }, () -> {});
        return result;
    }

/*    @DeleteMapping("/reply")
    public CommonDTO deleteReply(Reply reply) {
        CommonDTO result = new CommonDTO();
        replyRepository.findById(reply.getReplyId()).ifPresentOrElse(temp -> {
            replyRepository.save(reply);
        }, () -> {});
        return result;
    }*/
}
