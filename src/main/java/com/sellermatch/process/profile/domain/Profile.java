package com.sellermatch.process.profile.domain;

import com.sellermatch.util.Util;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ProfileList")
@Getter
@Setter
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_idx")
    private Integer profileIdx;

    @Column(name = "profile_id")
    private String profileId;

    @Column(name = "profile_mem_id")
    private String profileMemId;

    @Column(name = "profile_grade", columnDefinition = "char")
    private String profileGrade;

    @Column(name = "profile_intro", columnDefinition = "TEXT")
    private String profileIntro;

    @Column(name = "profile_ch", columnDefinition = "char")
    private String profileCh;

    @Column(name = "profile_ch_chk", columnDefinition = "char")
    private String profileChChk;

    @Column(name = "profile_ch_chk_date")
    private Date profileChChkDate;

    @Column(name = "profile_career", columnDefinition = "char")
    private String profileCareer;

    @Column(name = "profile_sale_chk", columnDefinition = "char")
    private String profileSaleChk;

    @Column(name = "profile_volume")
    private Long profileVolume;

    @Column(name = "profile_nation")
    private String profileNation;

    @Column(name = "profile_biz_num")
    private String profileBizNum;

    @Column(name = "profile_biz_sort", columnDefinition = "char")
    private String profileBizSort;

    @Column(name = "profile_biz_certi", columnDefinition = "char")
    private String profileBizCerti;

    @Column(name = "profile_indus")
    private String profileIndus;

    @Column(name = "profile_photo")
    private String profilePhoto;

    @Column(name = "profile_state", columnDefinition = "char")
    private String profileState;

    @Column(name = "profile_reg_date")
    private Date profileRegDate;

    @Column(name = "profile_edit_date")
    private Date profileEditDate;

    @Column(name = "profile_hashtag")
    private String profileHashtag;

    @Column(name = "profile_rname", columnDefinition = "char")
    private String profileRname;

    @Column(name = "profile_sort", columnDefinition = "char")
    private String profileSort;

    @Column(name = "profile_hit")
    private Integer profileHit;

    @Transient
    private Long projAddCount;

    @Transient
    private Long recommendCount;

    @Transient
    private Long contractCount;

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
    private String profileIndusName;

    @Transient
    private String memNick;

    @Transient
    private String memRname;

    @Transient
    private String memState;

    @Getter(AccessLevel.NONE)
    @Transient
    private String hashtaglist;

    @Getter(AccessLevel.NONE)
    @Transient
    private String tagRemoveProfileIntro;

    /** ????????? ?????? ?????? */
    @Transient
    private String[] profileNationArr;
    @Transient
    private String[] profileIndusArr;
    @Transient
    private String[] profileChannelArr;
    @Transient
    private String[] profileBizSortArr;
    @Transient
    private String[] profileCareerArr;
    @Transient
    private String[] profileVolumeArr;
    @Transient
    private String[]  profileSellerAuthArr;

    @Transient
    private Long scrapCount;
    @Transient
    private Long pRecommandCount;
    @Transient
    private Long sRecommandCount;
    @Transient
    private Long RecommandCount;
    @Transient
    private Long projectEndCount;
    @Transient
    private Long appliedCount;
    @Transient
    private String memSort;
    @Transient
    private Integer memIdx;
    @Transient
    private String projProdCerti;
    @Transient
    private String projProfit;


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

    public String getTagRemoveProfileIntro() {
        if (!Util.isEmpty(this.profileIntro)) {
            tagRemoveProfileIntro = this.profileIntro.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
            return tagRemoveProfileIntro.replace(System.getProperty("line.separator").toString(), "");
        }
        return "";
    }
}
