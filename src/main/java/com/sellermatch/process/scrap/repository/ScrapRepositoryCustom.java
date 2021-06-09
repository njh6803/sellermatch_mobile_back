package com.sellermatch.process.scrap.repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.SimplePath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sellermatch.process.apply.domain.QApply;
import com.sellermatch.process.member.domain.QMember;
import com.sellermatch.process.profile.domain.QProfile;
import com.sellermatch.process.project.domain.QProject;
import com.sellermatch.process.scrap.domain.QScrap;
import com.sellermatch.process.scrap.domain.Scrap;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ScrapRepositoryCustom {
    private final JPAQueryFactory query;
    private final QScrap qScrap = QScrap.scrap;
    private final QProject qProject = QProject.project;
    private final QApply qApply = QApply.apply;
    private final QMember qMember = QMember.member;
    private final QProfile qProfile = QProfile.profile;

    public Page<Scrap> getScrapList(Integer memIdx, Pageable pageable) {

        JPAQuery jpaQuery = selectScrapList(qScrap, qProject, qProfile, qMember, qApply, memIdx, pageable);
        return new PageImpl<>(jpaQuery.fetch(), pageable, jpaQuery.fetchCount());
    }

    private OrderSpecifier[] getSortedColumn(Sort sorts, QScrap qScrap){
        return sorts.toList().stream().map(x ->{
            Order order = x.getDirection().name() == "ASC"? Order.ASC : Order.DESC;
            SimplePath<Object> filedPath = Expressions.path(Object.class, qScrap, x.getProperty());
            return new OrderSpecifier(order, filedPath);
        }).toArray(OrderSpecifier[]::new);
    }

    private JPAQuery selectScrapList(QScrap qScrap, QProject qProject, QProfile qProfile, QMember qMember, QApply qApply, Integer memIdx, Pageable pageable) {
        JPAQuery jpaQuery = query.select(Projections.fields(Scrap.class,
                qScrap.scrapNo,
                qScrap.memIdx,
                qScrap.projIdx,
                qProject.projId,
                qProject.projRegDate,
                qProject.projTitle,
                qProject.projEndDate,
                qProject.projRecruitNum,
                qProject.projState,
                qProject.projSort,
                qProject.projNation,
                qProject.projSupplyType,
                qProject.projChannel,
                ExpressionUtils.as(
                        JPAExpressions.select(qApply.applyIdx.count())
                                .from(qApply)
                                .where(qApply.applyProjId.eq(qProject.projId))
                        ,"applyCount"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qMember.memNick)
                                .from(qMember)
                                .where(qMember.memId.eq(qProject.projMemId))
                        ,"memNick"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qApply.applyProjState)
                                .from(qApply)
                                .where(qApply.applyMemId.eq(qMember.memId).and(qApply.applyProjId.eq(qProject.projId).and(qApply.applyType.eq("1"))))
                        ,"applyProjState"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qApply.applyType)
                                .from(qApply)
                                .where(qApply.applyMemId.eq(qMember.memId).and(qApply.applyProjId.eq(qProject.projId).and(qApply.applyType.eq("1"))))
                        ,"applyType"
                )))
                .from(qScrap)
                .join(qProject).on(qScrap.projIdx.eq(qProject.projIdx))
                .join(qProfile).on(qProject.projMemId.eq(qProfile.profileMemId))
                .join(qMember).on(qScrap.memIdx.eq(qMember.memIdx))
                .where(qMember.memIdx.eq(memIdx))
                .orderBy(getSortedColumn(pageable.getSort(), qScrap))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        return jpaQuery;
    }
}
