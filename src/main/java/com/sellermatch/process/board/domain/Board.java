package com.sellermatch.process.board.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "BOARDLIST")
@Getter
@Setter
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_idx")
    private Integer boardIdx;

    @Column(name = "board_id")
    private String boardId;

    @Column(name = "board_title")
    private String boardTitle;

    @Column(name = "board_contents")
    private String boardContents;

    @Column(name = "board_writer")
    private String boardWriter;

    @Column(name = "board_type")
    private String boardType;

    @Column(name = "board_qa_type")
    private String boardQaType;

    @Column(name = "board_email")
    private String boardEmail;

    @Column(name = "board_hit")
    private Integer boardHit;

    @Column(name = "board_reg_date")
    private Date boardRegDate;

    @Column(name = "board_edit_date")
    private Date boardEditDate;

    @Column(name = "board_file")
    private Date boardFile;

    @Column(name = "board_notice_top")
    private Date boardNoticeTop;
}
