package com.sellermatch.process.hashtag.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "HASHTAGLIST")
@Getter
@Setter
public class Hashtaglist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hash_id")
    private Integer hashId;

    @Column(name = "hash_nm")
    private String hashNm;

    @Column(name = "frst_regist_mngr")
    private String frstRegistMngr;

    @Column(name = "frst_regist_dt")
    private Date frstRegistDt;

    @Column(name = "last_regist_mngr")
    private String lastRegistMngr;

    @Column(name = "last_regist_dt")
    private Date lastRegistDt;
}
