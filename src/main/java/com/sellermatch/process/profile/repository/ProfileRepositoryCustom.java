package com.sellermatch.process.profile.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.SimplePath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sellermatch.process.apply.domain.QApply;
import com.sellermatch.process.hashtag.domain.QHashtag;
import com.sellermatch.process.hashtag.domain.QHashtaglist;
import com.sellermatch.process.indus.domain.QIndus;
import com.sellermatch.process.member.domain.QMember;
import com.sellermatch.process.profile.domain.Profile;
import com.sellermatch.process.profile.domain.QProfile;
import com.sellermatch.process.project.domain.QProject;
import com.sellermatch.process.scrap.domain.QScrap;
import com.sellermatch.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProfileRepositoryCustom {
    private final JPAQueryFactory query;
    private final QProfile qProfile = QProfile.profile;
    //조인
    private final QMember qMember = QMember.member;
    // 서브쿼리
    private final QHashtag qHashtag = QHashtag.hashtag;
    private final QHashtaglist qHashtaglist = QHashtaglist.hashtaglist;
    private final QProject qProject = QProject.project;
    private final QApply qApply = QApply.apply;
    private final QIndus qIndus = QIndus.indus;
    private final QScrap qScrap = QScrap.scrap;


    public Profile getMyProjectCount(String projMemId) {
        Profile profile = selectpMyProjectCount(qProject, qMember, qProfile, qApply, qScrap, projMemId);
        return profile;
    }

    public Profile findSeller(Integer profileIdx) {
        Profile profile = getProfile(qProfile, qMember, qProject, qApply, qHashtag, qHashtaglist, qIndus, profileIdx);
        return profile;
    }

    public Page<Profile> findAllSeller(Profile profile, Pageable pageable, String search) {
        BooleanBuilder builder = new BooleanBuilder();

        // 판매자리스트페이지 노출 필수조건
        profile.setProfileSort("2");
        if (!Util.isEmpty(profile.getProfileSort())){
            // 프로필 유형 - 판매자 : 2
            builder.and(qProfile.profileSort.eq(profile.getProfileSort()));
            // 회원 이메일인증(신원인증) - 인증 : 1
            //builder.and(qMember.memRname.eq("1"));
        }

        // 사업자유형 필터
        if (!Util.isEmpty(profile.getProfileBizSortArr())){
            builder.and(qProfile.profileBizSort.in(profile.getProfileBizSortArr()));
        }
        // 판매자검증 필터
        if (!Util.isEmpty(profile.getProfileSellerAuthArr())) {
            for (int i=0; i < profile.getProfileSellerAuthArr().length; i++){
                // 신원인증
                if (profile.getProfileSellerAuthArr()[i].equalsIgnoreCase("1")){
                    builder.and(qMember.memRname.eq("1")
                            .and(qProfile.profileSort.eq("2")));
                }
                // 사업자인증
                if (profile.getProfileSellerAuthArr()[i].equalsIgnoreCase("2")){
                    builder.and(qProfile.profileBizCerti.eq("1")
                            .and(qProfile.profileSort.eq("2")));
                }
                // 채널검증
                if (profile.getProfileSellerAuthArr()[i].equalsIgnoreCase("3")){
                    builder.and(qProfile.profileChChk.eq("1"));
                }
                // 매출검증
                if (profile.getProfileSellerAuthArr()[i].equalsIgnoreCase("4")){
                    builder.and(qProfile.profileSaleChk.eq("1"));
                }
            }
        }
        // 지역 필터
        if (!Util.isEmpty(profile.getProfileNationArr())) {
            builder.and(qProfile.profileNation.in(profile.getProfileNationArr()));
        }
        // 상품분류 필터
        if (!Util.isEmpty(profile.getProfileIndusArr())) {
            builder.and(qProfile.profileIndus.in(profile.getProfileIndusArr()));
        }
        // 판매채널 필터
        if (!Util.isEmpty(profile.getProfileChannelArr())) {
            BooleanBuilder builder2 = new BooleanBuilder();
            for (int i=0; i < profile.getProfileChannelArr().length; i++){
                builder2.or(qProfile.profileCh.contains(profile.getProfileChannelArr()[i]));
            }
            builder.and(builder2);
        }
        // 판매경력
        if (!Util.isEmpty(profile.getProfileCareerArr())){
            builder.and(qProfile.profileCareer.in(profile.getProfileCareerArr()));
        }

        // 매출규모
        if (!Util.isEmpty(profile.getProfileVolumeArr())) {
            BooleanBuilder builder2 = new BooleanBuilder();
            for (int i = 0; i < profile.getProfileVolumeArr().length; i++) {
                if (profile.getProfileVolumeArr()[i].equalsIgnoreCase("1")){
                    builder2.or(qProfile.profileVolume.lt(10000000));
                }
                if (profile.getProfileVolumeArr()[i].equalsIgnoreCase("2")){
                    builder2.or(qProfile.profileVolume.goe(10000000).and(qProfile.profileVolume.lt(30000000)));
                }
                if (profile.getProfileVolumeArr()[i].equalsIgnoreCase("3")){
                    builder2.or(qProfile.profileVolume.goe(30000000).and(qProfile.profileVolume.lt(50000000)));
                }
                if (profile.getProfileVolumeArr()[i].equalsIgnoreCase("4")){
                    builder2.or(qProfile.profileVolume.goe(50000000).and(qProfile.profileVolume.loe(100000000)));
                }
                if (profile.getProfileVolumeArr()[i].equalsIgnoreCase("5")){
                    builder2.or(qProfile.profileVolume.gt(100000000));
                }
            }
            builder.and(builder2);
        }

        // 검색 필터
        if (!Util.isEmpty(search)){
            builder.and(
                    qMember.memNick.contains(search)
                    .or(qProfile.profileIntro.contains(search))
            );
        }

        JPAQuery jpaQuery = getProfileList(qProfile, qMember, qProject, qApply, qHashtag, qHashtaglist, qIndus, builder, pageable);

        return new PageImpl<>(jpaQuery.fetch(), pageable, jpaQuery.fetchCount());
    }

    private OrderSpecifier[] getSortedColumn(Sort sorts, QProfile qProfile, QApply qApply){
        return sorts.toList().stream().map(x ->{
            Order order = x.getDirection().name() == "ASC"? Order.ASC : Order.DESC;
            SimplePath<Object> filedPath = Expressions.path(Object.class, qProfile, x.getProperty());
            if (x.getProperty().equalsIgnoreCase("recommendCount")){
                NumberPath<Long> aliasQuantity = Expressions.numberPath(Long.class, "recommendCount");
                return new OrderSpecifier(order, aliasQuantity);
            }
            return new OrderSpecifier(order, filedPath);
        }).toArray(OrderSpecifier[]::new);
    }

    private Profile getProfile(QProfile qProfile, QMember qMember,
                                QProject qProject, QApply qApply,
                                QHashtag qHashtag, QHashtaglist qHashtaglist,
                                QIndus qIndus, Integer profileIdx) {

        Profile profile = query.select(Projections.fields(Profile.class,
                qProfile.profileIdx,
                qProfile.profileId,
                qProfile.profileMemId,
                qProfile.profileIntro,
                qProfile.profileChChk,
                qProfile.profileCareer,
                qProfile.profileSaleChk,
                qProfile.profileNation,
                qProfile.profileBizSort,
                qProfile.profileBizCerti,
                qProfile.profileCh,
                qProfile.profileIndus,
                qProfile.profilePhoto,
                qProfile.profileState,
                qProfile.profileSort,
                qProfile.profileVolume,
                ExpressionUtils.as(
                        JPAExpressions.select(qIndus.indusName).distinct()
                                .from(qIndus)
                                .where(qIndus.indusId.eq(qProfile.profileIndus))
                        ,"profileIndusName"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qProject.projIdx.count())
                                .from(qProject)
                                .where(qProject.projMemId.eq(qProfile.profileMemId))
                        ,"projAddCount"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qApply.applyIdx.count())
                                .from(qApply)
                                .where(qApply.applyMemId.eq(qProfile.profileMemId).and(qApply.applyType.eq("2")))
                        ,"recommendCount"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qApply.applyIdx.count())
                                .from(qApply)
                                .where(qApply.applyMemId.eq(qProfile.profileMemId).and(qApply.applyProjState.eq("5")))
                        ,"contractCount"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qHashtaglist.hashNm).distinct()
                                .from(qHashtaglist)
                                .where(qHashtaglist.hashId.eq(
                                        query.select(qHashtag.hashTag1)
                                                .from(qHashtag)
                                                .where(qHashtag.id.eq(qProfile.profileId))
                                )), "hashTag1"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qHashtaglist.hashNm).distinct()
                                .from(qHashtaglist)
                                .where(qHashtaglist.hashId.eq(
                                        query.select(qHashtag.hashTag2)
                                                .from(qHashtag)
                                                .where(qHashtag.id.eq(qProfile.profileId))
                                )), "hashTag2"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qHashtaglist.hashNm).distinct()
                                .from(qHashtaglist)
                                .where(qHashtaglist.hashId.eq(
                                        query.select(qHashtag.hashTag3)
                                                .from(qHashtag)
                                                .where(qHashtag.id.eq(qProfile.profileId))
                                )), "hashTag3"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qHashtaglist.hashNm).distinct()
                                .from(qHashtaglist)
                                .where(qHashtaglist.hashId.eq(
                                        query.select(qHashtag.hashTag4)
                                                .from(qHashtag)
                                                .where(qHashtag.id.eq(qProfile.profileId))
                                )), "hashTag4"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qHashtaglist.hashNm).distinct()
                                .from(qHashtaglist)
                                .where(qHashtaglist.hashId.eq(
                                        query.select(qHashtag.hashTag5)
                                                .from(qHashtag)
                                                .where(qHashtag.id.eq(qProfile.profileId))
                                )), "hashTag5"
                ),
                qMember.memNick,
                qMember.memRname,
                qMember.memState))
                .from(qProfile)
                .join(qMember).on(qProfile.profileMemId.eq(qMember.memId))
                .join(qIndus).on(qProfile.profileIndus.eq(qIndus.indusId))
                .where(qProfile.profileIdx.eq(profileIdx).and(qProfile.profileSort.eq("2"))).fetchOne();
        return profile;
    }

    private JPAQuery getProfileList(QProfile qProfile, QMember qMember,
                                         QProject qProject, QApply qApply,
                                         QHashtag qHashtag, QHashtaglist qHashtaglist,
                                         QIndus qIndus, BooleanBuilder builder,
                                         Pageable pageable) {
        JPAQuery jpaQuery = query.select(Projections.fields(Profile.class,
                qProfile.profileIdx,
                qProfile.profileId,
                qProfile.profileMemId,
                qProfile.profileIntro,
                qProfile.profileChChk,
                qProfile.profileCareer,
                qProfile.profileSaleChk,
                qProfile.profileNation,
                qProfile.profileBizSort,
                qProfile.profileBizCerti,
                qProfile.profileCh,
                qProfile.profileIndus,
                qProfile.profilePhoto,
                qProfile.profileState,
                qProfile.profileRegDate,
                qProfile.profileEditDate,
                qProfile.profileSort,
                qProfile.profileVolume,
                ExpressionUtils.as(
                        JPAExpressions.select(qIndus.indusName).distinct()
                                .from(qIndus)
                                .where(qIndus.indusId.eq(qProfile.profileIndus))
                        ,"profileIndusName"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qProject.projIdx.count())
                                .from(qProject)
                                .where(qProject.projMemId.eq(qProfile.profileMemId))
                        ,"projAddCount"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qApply.applyIdx.count())
                                .from(qApply)
                                .where(qApply.applyMemId.eq(qProfile.profileMemId).and(qApply.applyType.eq("2")))
                        ,"recommendCount"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qApply.applyIdx.count())
                                .from(qApply)
                                .where(qApply.applyMemId.eq(qProfile.profileMemId).and(qApply.applyProjState.eq("5")))
                        ,"contractCount"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qHashtaglist.hashNm).distinct()
                                .from(qHashtaglist)
                                .where(qHashtaglist.hashId.eq(
                                        query.select(qHashtag.hashTag1)
                                                .from(qHashtag)
                                                .where(qHashtag.id.eq(qProfile.profileId))
                                )), "hashTag1"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qHashtaglist.hashNm).distinct()
                                .from(qHashtaglist)
                                .where(qHashtaglist.hashId.eq(
                                        query.select(qHashtag.hashTag2)
                                                .from(qHashtag)
                                                .where(qHashtag.id.eq(qProfile.profileId))
                                )), "hashTag2"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qHashtaglist.hashNm).distinct()
                                .from(qHashtaglist)
                                .where(qHashtaglist.hashId.eq(
                                        query.select(qHashtag.hashTag3)
                                                .from(qHashtag)
                                                .where(qHashtag.id.eq(qProfile.profileId))
                                )), "hashTag3"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qHashtaglist.hashNm).distinct()
                                .from(qHashtaglist)
                                .where(qHashtaglist.hashId.eq(
                                        query.select(qHashtag.hashTag4)
                                                .from(qHashtag)
                                                .where(qHashtag.id.eq(qProfile.profileId))
                                )), "hashTag4"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qHashtaglist.hashNm).distinct()
                                .from(qHashtaglist)
                                .where(qHashtaglist.hashId.eq(
                                        query.select(qHashtag.hashTag5)
                                                .from(qHashtag)
                                                .where(qHashtag.id.eq(qProfile.profileId))
                                )), "hashTag5"
                ),
                qMember.memNick,
                qMember.memRname,
                qMember.memState))
                .from(qProfile)
                .join(qMember).on(qProfile.profileMemId.eq(qMember.memId))
                .join(qIndus).on(qProfile.profileIndus.eq(qIndus.indusId))
                .where(builder)
                .orderBy(getSortedColumn(pageable.getSort(), qProfile, qApply))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        return jpaQuery;
    }

    private Profile selectpMyProjectCount(QProject qProject, QMember qMember, QProfile qProfile, QApply qApply, QScrap qScrap, String projMemId) {
        Profile profile = query.select(Projections.fields(Profile.class,
                qProfile.profileMemId,
                qMember.memSort,
                qMember.memIdx,
                qMember.memRname,
                qProfile.profileChChk,
                qProfile.profileSaleChk,
                ExpressionUtils.as(
                        JPAExpressions.select(qProject.projIdx.count())
                                .from(qProject)
                                .where(qProject.projMemId.eq(qProfile.profileMemId).and(qProject.projState.eq("1")))
                        , "projAddCount"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qApply.applyIdx.count())
                                .from(qApply)
                                .where(qApply.applyMemId.eq(qProfile.profileMemId).and(qApply.applyType.eq("1")))
                        , "appliedCount"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qScrap.scrapNo.count())
                                .from(qScrap)
                                .where(qScrap.memIdx.eq(qMember.memIdx))
                        , "scrapCount"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qApply.applyIdx.count())
                                .from(qApply)
                                .join(qProject).on(qApply.applyProjId.eq(qProject.projId))
                                .where(qProject.projMemId.eq(qApply.applyMemId).and(qApply.applyType.eq("2")))
                        , "pRecommandCount"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qApply.applyIdx.count())
                                .from(qApply)
                                .where(qApply.applyMemId.eq(qMember.memId).and(qApply.applyType.eq("2")))
                        , "sRecommandCount"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qProject.projIdx.count())
                                .from(qProject)
                                .where(qProject.projMemId.eq(qProfile.profileMemId).and(qProject.projState.eq("2")))
                        , "projectEndCount"
                )))
                .from(qProfile)
                .join(qMember).on(qProfile.profileMemId.eq(qMember.memId))
                .where(qProfile.profileMemId.eq(projMemId)).fetchOne();

        return profile;
    }
}
