package com.sellermatch.process.withdraw.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TB_WITHDRAW_REASON")
@Getter
@Setter
public class Withdraw {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "withdraw_idx")
    private Integer withdrawIdx;

    @Column(name = "mem_idx")
    private Integer memIdx;

    @Column(name = "mem_id")
    private String memId;

    @Column(name = "withdraw_reason")
    private String withdrawReason;

    @Column(name = "withdraw_reason_text")
    private String withdrawReasonText;

    @Column(name = "withdraw_date")
    private Date withdrawDate;

    @Transient
    private String widthdrawAuthCode;
}
