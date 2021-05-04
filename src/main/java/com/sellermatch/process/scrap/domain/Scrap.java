package com.sellermatch.process.scrap.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TB_SCRAP")
@Getter
@Setter
public class Scrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scrap_no")
    private Integer scrapNo;

    @Column(name = "mem_idx")
    private Integer memIdx;

    @Column(name = "proj_idx")
    private Integer projIdx;

    @Column(name = "frst_regist_mngr")
    private String frstRegistMngr;

    @Column(name = "frst_regist_dt")
    private Date frstRegistDt;

    @Column(name = "last_regist_mngr")
    private String lastRegistMngr;

    @Column(name = "last_regist_dt")
    private Date lastRegistDt;

}
