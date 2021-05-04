package com.sellermatch.process.reply.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name ="REPLYLIST")
@Getter
@Setter
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_idx")
    private Integer replyIdx;

    @Column(name = "reply_parent")
    private Integer replyParent;

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

    @Column(name = "reply_secret")
    private String replySecret;

    @Column(name = "reply_depth")
    private String replyDepth;

    @Column(name = "reply_parent_mem_id")
    private String replyParentMemId;
}
