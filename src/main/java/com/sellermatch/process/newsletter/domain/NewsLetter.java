package com.sellermatch.process.newsletter.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "NewsLetterList")
@Getter
@Setter
public class NewsLetter {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "newsLetter_idx")
    private Integer newsLetterIdx;

    @Column(name = "NewsLetter_email")
    private String NewsLetterEmail;

    @Column(name = "NewsLetter_agreeYN", columnDefinition = "char")
    private String NewsLetterAgreeYN;

    @Column(name = "NewsLetter_reg_date")
    private Date NewsLetterRegDate;

    @Column(name = "NewsLetter_edit_date")
    private Date NewsLetterEditDate;
}
