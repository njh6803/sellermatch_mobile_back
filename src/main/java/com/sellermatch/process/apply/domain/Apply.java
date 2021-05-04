package com.sellermatch.process.apply.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "APPLYLIST")
@Getter
@Setter
public class Apply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appply_idx")
    private Integer applyIdx;

    @Column(name = "appply_id")
    private String applyId;

    @Column(name = "appply_proj_id")
    private String applyProjId;

    @Column(name = "appply_mem_id")
    private String applyMemId;

    @Column(name = "appply_reg_date")
    private Date applyRegDate;

    @Column(name = "appply_profile")
    private String applyProfile;

    @Column(name = "appply_type")
    private String applyType;

    @Column(name = "appply_proj_state")
    private String applyProjState;

    @Column(name = "appply_update_date")
    private Date applyUpdateDate;
}
