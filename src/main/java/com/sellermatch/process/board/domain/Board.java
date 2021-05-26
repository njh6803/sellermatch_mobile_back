package com.sellermatch.process.board.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "BoardList")
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

    @Column(name = "board_contents", columnDefinition = "TEXT")
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
    private String boardFile;

    @Column(name = "board_notice_top")
    private String boardNoticeTop;

    @Formula("(SELECT A.Mem_nick FROM MemList AS A WHERE board_writer = A.Mem_Id)")
    private String memNick;

    @Formula("(SELECT A.Mem_sort FROM MemList AS A WHERE board_writer = A.Mem_Id)")
    private String memSort;
}
