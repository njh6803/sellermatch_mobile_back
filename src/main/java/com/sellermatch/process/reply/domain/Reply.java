package com.sellermatch.process.reply.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name ="ReplyList")
@Getter
@Setter
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Integer replyId;

    @Column(name = "reply_parent")
    private Integer replyParent;

    @Column(name = "reply_writer")
    private String replyWriter;

    @Column(name = "reply_contents")
    private String replyContents;

    @Column(name = "reply_proj_id")
    private String replyProjId;

    @Column(name = "reply_board_id")
    private String replyBoardId;

    @Column(name = "reply_reg_date")
    private Date replyRegDate;

    @Column(name = "reply_pw")
    private String replyPw;

    @Column(name = "reply_secret", columnDefinition = "char")
    private String replySecret;

    @Column(name = "reply_depth", columnDefinition = "char")
    private String replyDepth;

    @Column(name = "reply_parent_mem_id")
    private String replyParentMemId;

    @Transient
    private String replyParentNick;
}
