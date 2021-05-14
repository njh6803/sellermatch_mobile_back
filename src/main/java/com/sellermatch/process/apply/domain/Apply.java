package com.sellermatch.process.apply.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ApplyList")
@Getter
@Setter
public class Apply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apply_idx")
    private Integer applyIdx;

    @Column(name = "apply_id")
    private String applyId;

    @Column(name = "apply_proj_id")
    private String applyProjId;

    @Column(name = "apply_mem_id")
    private String applyMemId;

    @Column(name = "apply_reg_date")
    private Date applyRegDate;

    @Column(name = "apply_profile")
    private String applyProfile;

    @Column(name = "apply_type", columnDefinition = "char")
    private String applyType;

    @Column(name = "apply_proj_state", columnDefinition = "char")
    private String applyProjState;

    @Column(name = "apply_update_date")
    private Date applyUpdateDate;
}
