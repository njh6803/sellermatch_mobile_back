package com.sellermatch.process.NewsLetter.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "NEWSLETTER")
@Getter
@Setter
public class NewsLetter {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "newsLetter_idx")
    private Integer newsLetterIdx;

    @Column(name = "NewsLetter_email")
    private String NewsLetterEmail;

    @Column(name = "NewsLetter_agreeYN")
    private String NewsLetterAgreeYN;

    @Column(name = "NewsLetter_reg_date")
    private String NewsLetterRegDate;

    @Column(name = "NewsLetter_edit_date")
    private String NewsLetterEditDate;
}
