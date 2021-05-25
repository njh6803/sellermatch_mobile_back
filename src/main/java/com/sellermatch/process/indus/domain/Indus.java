package com.sellermatch.process.indus.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "IndusList")
public class Indus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "indus_idx")
    private Integer indusIdx;

    @Column(name = "indus_id", columnDefinition = "char")
    private String indusId;

    @Column(name = "indus_name")
    private String indusName;

    @Column(name = "indus_reg_date")
    private String indusRegDate;
}
