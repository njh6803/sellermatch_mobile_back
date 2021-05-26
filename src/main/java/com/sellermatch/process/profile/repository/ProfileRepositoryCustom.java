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

    public Page<Profile> findAllSeller(Profile profile, Pageable pageable, String keyword) {
        profile.setProfileSort("2");
        QProfile qProfile = QProfile.profile;
        // 조인
        QMember qMember = QMember.member;

        // 서브쿼리
        QHashtag qHashtag = QHashtag.hashtag;
        QHashtaglist qHashtaglist = QHashtaglist.hashtaglist;
        QProject qProject = QProject.project;
        QApply qApply = QApply.apply;
        QIndus qIndus = QIndus.indus;

        pageable.getSort();

        BooleanBuilder builder = new BooleanBuilder();
        BooleanBuilder orderBuilder = new BooleanBuilder();

        // 판매자리스트페이지 노출 조건
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
        // 채널검증 필터
        if (!Util.isEmpty(profile.getProfileChChk())){
            builder.and(qProfile.profileChChk.eq(profile.getProfileChChk()));
        }
        // 매출검증 필터
        if (!Util.isEmpty(profile.getProfileSaleChk())){
            builder.and(qProfile.profileSaleChk.eq(profile.getProfileSaleChk()));
        }
        // 사업자인증 필터
        if (!Util.isEmpty(profile.getProfileBizCerti())){
            builder.and(qProfile.profileBizCerti.eq(profile.getProfileBizCerti()));
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
            //if (profile.getProfileVolumeArr())
            builder.and(qProfile.profileVolume.goe(0));
        }

        // 검색 필터
        if (!Util.isEmpty(keyword)){
            builder.and(
                    qMember.memNick.contains(keyword)
                    .or(qProfile.profileIndus.contains(keyword))
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
                        JPAExpressions.select(qIndus.indusName)
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
                                .where(qApply.applyMemId.eq(qProfile.profileMemId))
                                .where(qApply.applyType.eq("2"))
                        ,"recommendCount"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qApply.applyIdx.count())
                                .from(qApply)
                                .where(qApply.applyMemId.eq(qProfile.profileMemId))
                                .where(qApply.applyProjState.eq("5"))
                        ,"contractCount"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qHashtaglist.hashNm)
                                .from(qHashtaglist)
                                .where(qHashtaglist.hashId.eq(
                                        query.select(qHashtag.hashTag1)
                                                .from(qHashtag)
                                                .where(qHashtag.id.eq(qProfile.profileId))
                                )), "hashTag1"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qHashtaglist.hashNm)
                                .from(qHashtaglist)
                                .where(qHashtaglist.hashId.eq(
                                        query.select(qHashtag.hashTag2)
                                                .from(qHashtag)
                                                .where(qHashtag.id.eq(qProfile.profileId))
                                )), "hashTag2"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qHashtaglist.hashNm)
                                .from(qHashtaglist)
                                .where(qHashtaglist.hashId.eq(
                                        query.select(qHashtag.hashTag3)
                                                .from(qHashtag)
                                                .where(qHashtag.id.eq(qProfile.profileId))
                                )), "hashTag3"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qHashtaglist.hashNm)
                                .from(qHashtaglist)
                                .where(qHashtaglist.hashId.eq(
                                        query.select(qHashtag.hashTag4)
                                                .from(qHashtag)
                                                .where(qHashtag.id.eq(qProfile.profileId))
                                )), "hashTag4"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qHashtaglist.hashNm)
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
}
