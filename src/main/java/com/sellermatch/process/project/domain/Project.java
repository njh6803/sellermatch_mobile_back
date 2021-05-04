package com.sellermatch.process.project.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PROJECTLIST")
@Getter
@Setter
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proj_idx")
    private Integer projIdx;

    @Column(name = "proj_id")
    private String projId;

    @Column(name = "proj_mem_id")
    private String projMemId;

    @Column(name = "proj_title")
    private String projTitle;

    @Column(name = "proj_sort")
    private String projSort;

    @Column(name = "proj_indus")
    private String projIndus;

    @Column(name = "proj_price")
    private String projPrice;

    @Column(name = "proj_margin")
    private String projMargin;

    @Column(name = "proj_nation")
    private String projNation;

    @Column(name = "proj_supply_type")
    private String projSupplyType;

    @Column(name = "proj_end_date")
    private Date projEndDate;

    @Column(name = "proj_recuruit_num")
    private Integer projRecuruitNum;

    @Column(name = "proj_detail")
    private String projDetail;

    @Column(name = "proj_require")
    private String projRequire;

    @Column(name = "proj_keyword")
    private String projKeyword;

    @Column(name = "proj_detail_img")
    private String projDetailImg;

    @Column(name = "proj_file")
    private String projFile;

    @Column(name = "proj_state")
    private String projState;

    @Column(name = "proj_reg_date")
    private Date projRegDate;

    @Column(name = "proj_edit_date")
    private Date projEditDate;

    @Column(name = "proj_thumbnail_img")
    private String projThumbnailImg;

    @Column(name = "proj_prod_certi")
    private String projProdCerti;

    @Column(name = "proj_hit")
    private Integer projHit;

    @Column(name = "proj_channel")
    private String projChannel;

    @Column(name = "proj_profit")
    private String projProfit;

    @Column(name = "proj_profit_chk_date")
    private Date projProfitChkDate;
}
