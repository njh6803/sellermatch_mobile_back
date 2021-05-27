package com.sellermatch.process.tbCmmnCode.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TB_CMMN_CODE")
@Getter
@Setter
public class TbCmmnCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cmmn_code_no")
    private Integer cmmnCodeNo;

    @Column(name = "cmmn_code_ty")
    private String cmmnCodeTy;

    @Column(name = "cmmn_code_ko_nm")
    private String cmmnCodeKoNm;

    @Column(name = "cmmn_code_value")
    private String cmmnCodeValue;

    @Column(name = "cmmn_code_value_ko_nm")
    private String cmmnCodeValueKoNm;

    @Column(name = "frst_regist_mngr")
    private String frstRegistMngr;

    @Column(name = "frst_regist_dt")
    private Date frstRegistDt;

    @Column(name = "last_regist_mngr")
    private String lastRegistMngr;

    @Column(name = "last_regist_dt")
    private Date lastRegistDt;
}
