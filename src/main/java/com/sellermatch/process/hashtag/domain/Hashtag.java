package com.sellermatch.process.hashtag.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "HashTag")
@Getter
@Setter
public class Hashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    private Integer no;

    @Column(name = "id")
    private String id;

    @Column(name = "hash_type", columnDefinition = "char")
    private String hashType;

    @Column(name = "hash_tag1")
    private Integer hashTag1;

    @Column(name = "hash_tag2")
    private Integer hashTag2;

    @Column(name = "hash_tag3")
    private Integer hashTag3;

    @Column(name = "hash_tag4")
    private Integer hashTag4;

    @Column(name = "hash_tag5")
    private Integer hashTag5;

    @Column(name = "frst_regist_mngr")
    private String frstRegistMngr;

    @Column(name = "frst_regist_dt")
    private Date frstRegistDt;

    @Column(name = "last_regist_mngr")
    private String lastRegistMngr;

    @Column(name = "last_regist_dt")
    private Date lastRegistDt;

    @Transient
    private List<String> hashNmList;
}
