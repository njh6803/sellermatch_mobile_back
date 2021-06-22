package com.sellermatch.process.reply.controller;

import com.sellermatch.process.common.domain.CommonConstant;
import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.reply.domain.Reply;
import com.sellermatch.process.reply.repository.ReplyRepository;
import com.sellermatch.process.reply.repository.ReplyRepositoryCustom;
import com.sellermatch.util.ControllerResultSet;
import com.sellermatch.util.MailUtil;
import com.sellermatch.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api-v1")
public class ReplyController {

    private final ReplyRepository replyRepository;
    private final ReplyRepositoryCustom replyRepositoryCustom;
    private final MailUtil mailUtil;

    @GetMapping("/reply/{id}")
    public CommonDTO selectReply(@PathVariable Integer id) {
        CommonDTO result = new CommonDTO();

        replyRepository.findById(id).ifPresentOrElse(temp -> {
            result.setContent(temp);
        }, () -> {
            Reply emptyContent =  new Reply();
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_998, emptyContent);
        });
        return result;
    }

    @GetMapping("/reply/project/list/{projId}")
    public CommonDTO selectProjectReplyList(@PathVariable String projId, Pageable pageable) {
        CommonDTO result = new CommonDTO();
        Reply reply = new Reply();
        reply.setReplyProjId(projId);
        Page<Reply> replyList = replyRepositoryCustom.getReplyList(reply, pageable);
        result.setContent(replyList);
        return result;
    }

    @GetMapping("/reply/board/list/{boardId}")
    public CommonDTO selectBoardReplyList(@PathVariable String boardId, Pageable pageable) {
        CommonDTO result = new CommonDTO();
        Reply reply = new Reply();
        reply.setReplyBoardId(boardId);
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
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_149);
            return result;
        }
        //댓글내용: 길이 체크 (1000자)
        if(!Util.isLengthChk(reply.getReplyContents(),0,1000)){
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_LENGTH_151);
            return result;
        }

        if (!Util.isEmpty(reply.getReplyParentMemId())) {
            reply.setReplyParentMemId(reply.getReplyParentMemId());
        } else {
            reply.setReplyParentMemId(reply.getReplyWriter());
        }

        String to = reply.getReplyParentMemId();
        String mailType = "replyMail";
        String subject = "";
        if (!Util.isEmpty(reply.getReplyParent()) && reply.getReplyParent() != 0) {
            reply.setReplyParent(reply.getReplyParent());
            reply.setReplyDepth("1");
            subject = "셀러매치 답글알림";
            //mailUtil.sendMail(to, subject, mailType, reply.getReplyContents());
        } else {
            reply.setReplyParent(replyRepository.getSeq()+1);
            reply.setReplyDepth("0");
            subject = "셀러매치 댓글알림";
            //mailUtil.sendMail(to, subject, mailType, reply.getReplyContents());
        }
        reply.setReplyRegDate(new Date());

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
