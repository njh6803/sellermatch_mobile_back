package com.sellermatch.process.profile.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ProfileList")
@Getter
@Setter
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_idx")
    private Integer profileIdx;

    @Column(name = "profile_id")
    private String profileId;

    @Column(name = "profile_mem_id")
    private String profileMemId;

    @Column(name = "profile_grade", columnDefinition = "char")
    private String profileGrade;

    @Column(name = "profile_intro", columnDefinition = "TEXT")
    private String profileIntro;

    @Column(name = "profile_ch", columnDefinition = "char")
    private String profileCh;

    @Column(name = "profile_ch_chk", columnDefinition = "char")
    private String profileChChk;

    @Column(name = "profile_ch_chk_date")
    private Date profileChChkDate;

    @Column(name = "profile_career", columnDefinition = "char")
    private String profileCareer;

    @Column(name = "profile_sale_chk", columnDefinition = "char")
    private String profileSaleChk;

    @Column(name = "profile_volume")
    private Integer profileVolume;

    @Column(name = "profile_nation")
    private String profileNation;

    @Column(name = "profile_biz_num")
    private String profileBizNum;

    @Column(name = "profile_biz_sort", columnDefinition = "char")
    private String profileBizSort;

    @Column(name = "profile_biz_certi", columnDefinition = "char")
    private String profileBizCerti;

    @Column(name = "profile_indus")
    private String profileIndus;

    @Column(name = "profile_photo")
    private String profilePhoto;

    @Column(name = "profile_state", columnDefinition = "char")
    private String profileState;

    @Column(name = "profile_reg_date")
    private Date profileRegDate;

    @Column(name = "profile_edit_date")
    private Date profileEditDate;

    @Column(name = "profile_hashtag")
    private String profileHashtag;

    @Column(name = "profile_rname", columnDefinition = "char")
    private String profileRname;

    @Column(name = "profile_sort", columnDefinition = "char")
    private String profileSort;

    @Column(name = "profile_hit")
    private Integer profileHit;
}
