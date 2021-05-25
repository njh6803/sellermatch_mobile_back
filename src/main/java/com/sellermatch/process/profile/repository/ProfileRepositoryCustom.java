package com.sellermatch.process.profile.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
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
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ProfileRepositoryCustom {
    private final JPAQueryFactory query;

    public List<Profile> findAllSeller(Profile profile) {
        QProfile qProfile = QProfile.profile;
        // 조인
        QMember qMember = QMember.member;

        // 서브쿼리
        QHashtag qHashtag = QHashtag.hashtag;
        QHashtaglist qHashtaglist = QHashtaglist.hashtaglist;
        QProject qProject = QProject.project;
        QApply qApply = QApply.apply;
        QIndus qIndus = QIndus.indus;

        BooleanBuilder builder = new BooleanBuilder();

        if (!Util.isEmpty(profile.getProfileSort())){
            builder.and(qProfile.profileSort.eq(profile.getProfileSort()));
        }
        if (!Util.isEmpty(profile.getMemNick()) || !Util.isEmpty(profile.getProfileIntro())){

        }

        List<Profile> jpaQuery = query.select(Projections.fields(Profile.class,
                                                        qProfile.profileIdx,
                                                        qProfile.profileId,
                                                        qProfile.profileMemId,
                                                        qProfile.profileIntro,
                                                        qProfile.profileChChk,
                                                        qProfile.profileCareer,
                                                        qProfile.profile.profileSaleChk,
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
                                                    .where(builder)
                                                    .fetch();
        return jpaQuery;
    }
}
