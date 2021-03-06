package com.sellermatch.process.project.domain;

import com.sellermatch.util.Util;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ProjectList")
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

    @Column(name = "proj_sort", columnDefinition = "char")
    private String projSort;

    @Column(name = "proj_indus")
    private String projIndus;

    @Column(name = "proj_price")
    private String projPrice;

    @Column(name = "proj_margin")
    private Integer projMargin;

    @Column(name = "proj_nation")
    private String projNation;

    @Column(name = "proj_supply_type")
    private String projSupplyType;

    @Column(name = "proj_end_date")
    private Date projEndDate;

    @Column(name = "proj_recruit_num")
    private Integer projRecruitNum;

    @Column(name = "proj_detail", columnDefinition = "TEXT")
    private String projDetail;

    @Column(name = "proj_require")
    private String projRequire;

    @Column(name = "proj_keyword")
    private String projKeyword;

    @Column(name = "proj_detail_img")
    private String projDetailImg;

    @Column(name = "proj_file")
    private String projFile;

    @Column(name = "proj_state", columnDefinition = "char")
    private String projState;

    @Column(name = "proj_reg_date")
    private Date projRegDate;

    @Column(name = "proj_edit_date")
    private Date projEditDate;

    @Column(name = "proj_thumbnail_img")
    private String projThumbnailImg;

    @Column(name = "proj_prod_certi", columnDefinition = "char")
    private String projProdCerti;

    @Column(name = "proj_hit")
    private Integer projHit;

    @Column(name = "proj_channel")
    private String projChannel;

    @Column(name = "proj_profit", columnDefinition = "char")
    private String projProfit;

    @Column(name = "proj_profit_chk_date")
    private Date projProfitChkDate;

    /** ????????? ?????? ?????? */
    @Transient
    private String[] projSortArr;
    @Transient
    private String[] projNationArr;
    @Transient
    private String[] projIndusArr;
    @Transient
    private String[] projPriceArr;
    @Transient
    private Integer[] projMarginArr;
    @Transient
    private String[] projSupplyTypeArr;
    @Transient
    private String[] projChannelArr;
    @Transient
    private String[] projectSupplyAuthArr;
    @Transient
    private String[] projectSellerAuthArr;

    @Transient
    private String originName;
    @Transient
    private String profileChChk;
    @Transient
    private String profileSaleChk;
    @Transient
    private String profileBizCerti;
    @Transient
    private String profilePhoto;
    @Transient
    private String memNick;
    @Transient
    private String memTel;
    @Transient
    private String memId;
    @Transient
    private String memRname;
    @Transient
    private String applyProjState;
    @Transient
    private Integer applyIdx;
    @Transient
    private String applyType;
    @Transient
    private String applyId;
    @Transient
    private String applyProjId;
    @Transient
    private Date applyRegDate;
    @Transient
    private Long projAddCount;
    @Transient
    private Long contractCount;
    @Transient
    private Long applyCount;
    @Transient
    private Long okeyCount;
    @Transient
    private Long scrapCheck;
    @Transient
    private String recommandProjectFlag;
    @Transient
    private String endProjectFlag;

    @Transient
    private String hashTag1;
    @Transient
    private String hashTag2;
    @Transient
    private String hashTag3;
    @Transient
    private String hashTag4;
    @Transient
    private String hashTag5;
    @Transient
    private Long replyCount;

    @Getter(AccessLevel.NONE)
    @Transient
    private String hashtaglist;

    @Getter(AccessLevel.NONE)
    @Transient
    private String tagRemoveProjDetail;

    public String getHashtaglist() {
        if (!Util.isEmpty(this.hashTag1)){
            hashtaglist = this.hashTag1;
        }
        if (!Util.isEmpty(this.hashTag2)){
            hashtaglist += "," + this.hashTag2;
        }
        if (!Util.isEmpty(this.hashTag3)){
            hashtaglist += "," + this.hashTag3;
        }
        if (!Util.isEmpty(this.hashTag4)){
            hashtaglist += "," + this.hashTag4;
        }
        if (!Util.isEmpty(this.hashTag5)){
            hashtaglist += "," + this.hashTag5;
        }
        return hashtaglist;
    }

    public String getTagRemoveProjDetail() {
        if (!Util.isEmpty(this.projDetail)) {
            tagRemoveProjDetail = this.projDetail.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
            return tagRemoveProjDetail.replace(System.getProperty("line.separator").toString(), "");
        }
        return "";
    }
}
