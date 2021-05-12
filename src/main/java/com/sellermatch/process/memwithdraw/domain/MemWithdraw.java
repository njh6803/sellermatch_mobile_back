package com.sellermatch.process.memwithdraw.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "TB_MEM_WITHDRAW")
@Getter
@Setter
public class MemWithdraw {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mem_withdraw_idx")
    private Integer memWithdrawIdx;

    @Column(name = "mem_idx")
    private Integer memIdx;

    @Column(name = "mem_id")
    private Integer memId;

    @Column(name = "mem_pw")
    private Integer memPw;

    @Column(name = "mem_state")
    private Integer memState;

    @Column(name = "mem_class")
    private Integer memClass;

    @Column(name = "mem_class_sdate")
    private Integer memClassSdate;

    @Column(name = "mem_class_edate")
    private Integer memClassEdate;

    @Column(name = "mem_sort")
    private Integer memSort;

    @Column(name = "mem_country")
    private Integer memCountry;

    @Column(name = "mem_nation")
    private Integer memNation;

    @Column(name = "mem_addr")
    private Integer memAddr;

    @Column(name = "mem_addr2")
    private Integer memAddr2;

    @Column(name = "mem_post")
    private Integer memPost;

    @Column(name = "mem_tel")
    private Integer memTel;

    @Column(name = "mem_name")
    private Integer memName;

    @Column(name = "mem_rname")
    private Integer memRname;

    @Column(name = "mem_nick")
    private Integer memNick;

    @Column(name = "mem_photo")
    private Integer memPhoto;

    @Column(name = "mem_sns_ch")
    private Integer memSnsCh;

    @Column(name = "googleid")
    private Integer googleId;

    @Column(name = "navedrid")
    private Integer navedrId;

    @Column(name = "kakaoid")
    private Integer kakaoId;

    @Column(name = "mem_sns_ch_tkn")
    private Integer memSnsChTkn;

    @Column(name = "mem_ip")
    private Integer memIp;

    @Column(name = "mem_login_date")
    private Integer memLoginDate;

    @Column(name = "mem_out_date")
    private Integer memOutDate;

    @Column(name = "mem_date")
    private Integer memDate;

    @Column(name = "mem_edit_date")
    private Integer memEditDate;

    @Column(name = "session_key")
    private Integer sessionKey;

    @Column(name = "session_limit")
    private Integer sessionLimit;
}
