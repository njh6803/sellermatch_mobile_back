package com.sellermatch.process.apply.repositiory;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.SimpleTemplate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sellermatch.process.apply.domain.Apply;
import com.sellermatch.process.apply.domain.QApply;
import com.sellermatch.process.hashtag.domain.QHashtag;
import com.sellermatch.process.hashtag.domain.QHashtaglist;
import com.sellermatch.process.member.domain.QMember;
import com.sellermatch.process.profile.domain.QProfile;
import com.sellermatch.process.project.domain.QProject;
import com.sellermatch.process.tbCmmnCode.domain.QTbCmmnCode;
import com.sellermatch.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ApplyRepositoryCustom {
    private final JPAQueryFactory query;
    private final QApply qApply = QApply.apply;
    private final QMember qMember = QMember.member;
    private final QProfile qProfile = QProfile.profile;
    private final QProject qProject = QProject.project;
    private final QHashtag qHashtag = QHashtag.hashtag;
    private final QHashtaglist qHashtaglist = QHashtaglist.hashtaglist;
    private final QTbCmmnCode qTbCmmnCode = QTbCmmnCode.tbCmmnCode;

    public Page<Apply> getApplyList(Apply apply, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();

        if (!Util.isEmpty(apply.getApplyMemId())) {
            builder.and(qApply.applyMemId.eq(apply.getApplyMemId()));
        }
        if (!Util.isEmpty(apply.getApplyProjId())) {
            builder.and(qApply.applyProjId.eq(apply.getApplyProjId()));
        }
        if (!Util.isEmpty(apply.getApplyType())) {
            builder.and(qApply.applyType.eq("1"));
        }
        if (!Util.isEmpty(apply.getApplyProjState())) {
            builder.and(qApply.applyProjState.eq(apply.getApplyProjState()));
        }

        JPAQuery jpaQuery = getApplyList(qApply, apply, pageable, builder);

        return new PageImpl<>(jpaQuery.fetch(), pageable, jpaQuery.fetchCount());
    }

    public Apply getAcceptedRecommandOwner(Apply apply) {

        Apply jpaQuery = getAcceptedRecommandOwner(qApply, apply, qMember, qProject);

        return jpaQuery;
    }

    public Apply getAcceptedProjectOwner(Apply apply) {

        Apply jpaQuery = getAcceptedProjectOwner(qApply, apply, qMember, qProject);

        return jpaQuery;
    }

    private JPAQuery getApplyList(QApply qApply, Apply apply, Pageable pageable, BooleanBuilder builder) {
        // GROUP_CONCAT(CMMN_CODE_VALUE_KO_NM)
        SimpleTemplate<String> simpleTemplate = Expressions.simpleTemplate(String.class, "group_concat({0})", qTbCmmnCode.cmmnCodeValueKoNm);
//        SimpleTemplate<String> simpleTemplate2 = Expressions.simpleTemplate(String.class, "if({0})", "1");

        JPAQuery jpaQuery = query.select(Projections.fields(Apply.class,
                qApply.applyIdx,
                qApply.applyId,
                qApply.applyProjId,
                qApply.applyMemId,
                qApply.applyProfile,
                qApply.applyType,
                qApply.applyRegDate,
                qApply.applyProjState,
                qMember.memIdx,
                qMember.memSort,
                qMember.memTel,
                qProfile.profilePhoto,
                qProfile.profileIntro,
                qProfile.profileBizSort,
                qProfile.profileVolume,
                qProject.projId,
                qProject.projTitle,
                qProject.projState,
                qProject.projRegDate,
                qProject.projEndDate,
                qProfile.profileCh,
                ExpressionUtils.as(
                        JPAExpressions.select(qTbCmmnCode.cmmnCodeValueKoNm)
                                .from(qTbCmmnCode)
                                .where(qTbCmmnCode.cmmnCodeTy.eq("A10")
                                .and(qTbCmmnCode.cmmnCodeValue.eq(qProfile.profileCareer)))
                        ,"profileCareer"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qMember.memNick)
                                .from(qMember)
                                .where(qMember.memId.eq(
                                        query.select(qProject.projMemId)
                                        .from(qProject)
                                        .where(qProject.projId.eq(qApply.applyProjId))
                                ))
                        ,"memNick"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qMember.memNick)
                                .from(qMember)
                                .where(qMember.memId.eq(qApply.applyMemId))
                        ,"applyMemNick"
                )))
                .from(qApply)
                .join(qMember).on(qApply.applyMemId.eq(qMember.memId))
                .join(qProfile).on(qApply.applyMemId.eq(qProfile.profileMemId))
                .join(qProject).on(qApply.applyProjId.eq(qProject.projId))
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qProject.projRegDate.desc());

        return jpaQuery;
    }

    private Apply getAcceptedRecommandOwner(QApply qApply, Apply apply, QMember qMember, QProject qProject) {

        Apply jpaQuery = query.select(Projections.fields(Apply.class,
                qApply.applyIdx,
                qMember.memId,
                qMember.memTel,
                qMember.memSort,
                qMember.memNick,
                qProject.projId,
                qProject.projTitle))
                .from(qApply)
                .join(qProject).on(qApply.applyProjId.eq(qProject.projId))
                .join(qMember).on(qProject.projMemId.eq(qMember.memId))
                .where(qProject.projId.eq(apply.getApplyProjId()).and(qApply.applyId.eq(apply.getApplyId())))
                .fetchOne();

        return jpaQuery;
    }

    private Apply getAcceptedProjectOwner(QApply qApply, Apply apply, QMember qMember, QProject qProject) {

        Apply jpaQuery = query.select(Projections.fields(Apply.class,
                qApply.applyIdx,
                qMember.memId,
                qMember.memTel,
                qMember.memSort,
                qMember.memNick,
                qProject.projId,
                qProject.projTitle))
                .from(qApply)
                .join(qProject).on(qApply.applyProjId.eq(qProject.projId))
                .join(qMember).on(qProject.projMemId.eq(qMember.memId))
                .where(qProject.projId.eq(qApply.applyProjId).and(qApply.applyId.eq(apply.getApplyId())))
                .fetchOne();

        return jpaQuery;
    }
}
