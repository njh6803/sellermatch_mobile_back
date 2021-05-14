package com.sellermatch.process.memwithdraw.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

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
    private String memId;

    @Column(name = "mem_pw")
    private String memPw;

    @Column(name = "mem_state", columnDefinition = "char")
    private String memState;

    @Column(name = "mem_class", columnDefinition = "char")
    private String memClass;

    @Column(name = "mem_class_sdate")
    private Date memClassSdate;

    @Column(name = "mem_class_edate")
    private Date memClassEdate;

    @Column(name = "mem_sort", columnDefinition = "char")
    private String memSort;

    @Column(name = "mem_country")
    private String memCountry;

    @Column(name = "mem_nation")
    private String memNation;

    @Column(name = "mem_addr")
    private String memAddr;

    @Column(name = "mem_addr2")
    private String memAddr2;

    @Column(name = "mem_post")
    private String memPost;

    @Column(name = "mem_tel")
    private String memTel;

    @Column(name = "mem_name")
    private String memName;

    @Column(name = "mem_rname", columnDefinition = "char")
    private String memRname;

    @Column(name = "mem_nick")
    private String memNick;

    @Column(name = "mem_photo")
    private String memPhoto;

    @Column(name = "mem_sns_ch")
    private String memSnsCh;

    @Column(name = "googleid")
    private String googleId;

    @Column(name = "naverid")
    private String naverId;

    @Column(name = "kakaoid")
    private String kakaoId;

    @Column(name = "mem_sns_ch_tkn")
    private String memSnsChTkn;

    @Column(name = "mem_ip")
    private String memIp;

    @Column(name = "mem_login_date")
    private Date memLoginDate;

    @Column(name = "mem_out_date")
    private Date memOutDate;

    @Column(name = "mem_date")
    private Date memDate;

    @Column(name = "mem_edit_date")
    private Date memEditDate;

    @Column(name = "session_key")
    private String sessionKey;

    @Column(name = "session_limit")
    private Date sessionLimit;
}
