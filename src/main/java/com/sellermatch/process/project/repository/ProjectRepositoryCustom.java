package com.sellermatch.process.project.repository;

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
import com.sellermatch.config.constant.ApplyType;
import com.sellermatch.config.constant.ProjectStateType;
import com.sellermatch.process.apply.domain.QApply;
import com.sellermatch.process.file.domain.QFile;
import com.sellermatch.process.hashtag.domain.QHashtag;
import com.sellermatch.process.hashtag.domain.QHashtaglist;
import com.sellermatch.process.indus.domain.QIndus;
import com.sellermatch.process.member.domain.QMember;
import com.sellermatch.process.profile.domain.QProfile;
import com.sellermatch.process.project.domain.Project;
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
public class ProjectRepositoryCustom {
    private final JPAQueryFactory query;
    private final QProject qProject = QProject.project;

    // 조인
    private final QMember qMember = QMember.member;
    private final QProfile qProfile = QProfile.profile;
    private final QFile qFile = QFile.file;

    // 서브쿼리
    private final QHashtag qHashtag = QHashtag.hashtag;
    private final QHashtaglist qHashtaglist = QHashtaglist.hashtaglist;
    private final QApply qApply = QApply.apply;
    private final QIndus qIndus = QIndus.indus;
    private final QScrap qScrap = QScrap.scrap;

    public Page<Project> getProjectEndList(String memId, Pageable pageable){
        JPAQuery jpaQuery = selectProjectEndList(qMember, qProject, qApply, memId, pageable);
        return new PageImpl<>(jpaQuery.fetch(), pageable, jpaQuery.fetchCount());
    }

    public Page<Project> getMyApplyList(String memId, Pageable pageable){
        JPAQuery jpaQuery = selectMyApplyList(qMember, qProject, qApply, memId, pageable);
        return new PageImpl<>(jpaQuery.fetch(), pageable, jpaQuery.fetchCount());
    }

    public Page<Project> getRecommandListForPro(String memId, Pageable pageable){
        JPAQuery jpaQuery = selectRecommandListForPro(qMember, qProject, qApply, memId, pageable);
        return new PageImpl<>(jpaQuery.fetch(), pageable, jpaQuery.fetchCount());
    }

    public Page<Project> getRecommandListForSell(String memId, Pageable pageable){
        JPAQuery jpaQuery = selectRecommandListForSell(qMember, qProject, qApply, memId, pageable);
        return new PageImpl<>(jpaQuery.fetch(), pageable, jpaQuery.fetchCount());
    }

    public Page<Project> getpRegistedProjectList(String memId, Pageable pageable) {
        JPAQuery jpaQuery = selectpRegistedProjectList(qProfile, qMember, qProject, qApply, memId, pageable);
        return new PageImpl<>(jpaQuery.fetch(), pageable, jpaQuery.fetchCount());
    }

    public Project findProject(Integer projectIdx, Integer memIdx) {
        Project project = getProject(qProfile, qMember, qProject, qApply, qHashtag, qHashtaglist, qIndus, qFile, projectIdx, memIdx);
        return project;
    }

    public Page<Project> findAllProject(Project project, Integer memIdx, Pageable pageable, String search) {
        BooleanBuilder builder = new BooleanBuilder();

        // 거래처매칭페이지 노출 필수조건
        builder.and(qProject.projState.ne(ProjectStateType.STOP.label)); // 프로젝트 상태 != 0

        // 마감이 안된것만 조회
        if (!Util.isEmpty(project.getRecommandProjectFlag())){
            builder.and(qProject.projState.eq(ProjectStateType.NORMAL.label));
        }

        // 찾기유형(공급자,판매자) 필터
        if (!Util.isEmpty(project.getProjSortArr())){
            builder.and(qProject.projSort.in(project.getProjSortArr()));
        }
        // 등록지역 필터
        if (!Util.isEmpty(project.getProjNationArr())) {
            builder.and(qProject.projNation.in(project.getProjNationArr()));
        }
        // 상품분류 필터
        if (!Util.isEmpty(project.getProjIndusArr())) {
            builder.and(qProject.projIndus.in(project.getProjIndusArr()));
        }
        // 상품단가 필터
        if (!Util.isEmpty(project.getProjPriceArr())) {
            builder.and(qProject.projPrice.in(project.getProjPriceArr()));
        }
        // 상품마진 필터
        if (!Util.isEmpty(project.getProjMarginArr())) {
            builder.and(qProject.projMargin.in(project.getProjMarginArr()));
        }
        // 공급방법 필터
        if (!Util.isEmpty(project.getProjSupplyTypeArr())) {
            builder.and(qProject.projSupplyType.in(project.getProjSupplyTypeArr()));
        }
        // 판매채널 필터
        if (!Util.isEmpty(project.getProjChannelArr())) {
            BooleanBuilder builder2 = new BooleanBuilder();
            for (int i=0; i < project.getProjChannelArr().length; i++){
                builder2.or(qProject.projChannel.contains(project.getProjChannelArr()[i]));
            }
            builder.and(builder2);
        }
        // 공급자검증 필터
        if (!Util.isEmpty(project.getProjectSupplyAuthArr())) {
            for (int i=0; i < project.getProjectSupplyAuthArr().length; i++){
                // 신원인증
                if (project.getProjectSupplyAuthArr()[i].equalsIgnoreCase("1")){
                    builder.and(qMember.memRname.eq("1")
                            .and(qProject.projSort.eq("1")));
                }
                // 사업자인증
                if (project.getProjectSupplyAuthArr()[i].equalsIgnoreCase("2")){
                    builder.and(qProfile.profileBizCerti.eq("1")
                            .and(qProject.projSort.eq("1")));
                }
                // 상품검증
                if (project.getProjectSupplyAuthArr()[i].equalsIgnoreCase("3")){
                    builder.and(qProject.projProdCerti.eq("1"));
                }
                // 수익성검증
                if (project.getProjectSupplyAuthArr()[i].equalsIgnoreCase("4")){
                    builder.and(qProject.projProfit.eq("1"));
                }
            }
        }

        // 판매자검증 필터
        if (!Util.isEmpty(project.getProjectSellerAuthArr())) {
            for (int i=0; i < project.getProjectSellerAuthArr().length; i++){
                // 신원인증
                if (project.getProjectSellerAuthArr()[i].equalsIgnoreCase("1")){
                    builder.and(qMember.memRname.eq("1")
                            .and(qProject.projSort.eq("2")));
                }
                // 사업자인증
                if (project.getProjectSellerAuthArr()[i].equalsIgnoreCase("2")){
                    builder.and(qProfile.profileBizCerti.eq("1")
                            .and(qProject.projSort.eq("2")));
                }
                // 채널검증
                if (project.getProjectSellerAuthArr()[i].equalsIgnoreCase("3")){
                    builder.and(qProfile.profileChChk.eq("1"));
                }
                // 매출검증
                if (project.getProjectSellerAuthArr()[i].equalsIgnoreCase("4")){
                    builder.and(qProfile.profileSaleChk.eq("1"));
                }
            }
        }

        // 검색 필터
        if (!Util.isEmpty(search)){
            builder.and(
                    qProject.projDetail.contains(search)
                    .or(qProject.projTitle.contains(search))
            );
        }


        if (!Util.isEmpty(project.getProjMemId())){
            builder.and(qProject.projMemId.eq(project.getProjMemId()));
        }

        JPAQuery jpaQuery = getProjectList(qProfile, qMember, qProject, qApply, qHashtag, qHashtaglist, qIndus, memIdx, builder, pageable);

        return new PageImpl<>(jpaQuery.fetch(), pageable, jpaQuery.fetchCount());
    }

    private OrderSpecifier[] getSortedColumn(Sort sorts, QProject qProject, QApply qApply){
        return sorts.toList().stream().map(x ->{
            Order order = x.getDirection().name() == "ASC"? Order.ASC : Order.DESC;
            SimplePath<Object> filedPath = Expressions.path(Object.class, qProject, x.getProperty());
            if (x.getProperty().equalsIgnoreCase("applyCount")){
                NumberPath<Long> aliasQuantity = Expressions.numberPath(Long.class, "applyCount");
                return new OrderSpecifier(order, aliasQuantity);
            }
            return new OrderSpecifier(order, filedPath);
        }).toArray(OrderSpecifier[]::new);
    }

    private Project getProject(QProfile qProfile, QMember qMember,
                                    QProject qProject, QApply qApply,
                                    QHashtag qHashtag, QHashtaglist qHashtaglist,
                                    QIndus qIndus, QFile qFile, Integer projectIdx, Integer memIdx) {

        Project project = query.select(Projections.fields(Project.class,
                qProject.projIdx,
                qProject.projId,
                qProject.projMemId,
                qProject.projTitle,
                qProject.projSort,
                qProject.projIndus,
                qProject.projPrice,
                qProject.projMargin,
                qProject.projNation,
                qProject.projSupplyType,
                qProject.projRecruitNum,
                qProject.projDetail,
                qProject.projThumbnailImg,
                qProject.projProdCerti,
                qProject.projRequire,
                qProject.projState,
                qProject.projRegDate,
                qProject.projEndDate,
                qProject.projProfit,
                qProject.projChannel,
                qProject.projFile,
                ExpressionUtils.as(
                        JPAExpressions.select(qFile.orginName).distinct()
                                .from(qFile)
                                .where(qFile.filePath.eq(qProject.projFile))
                        ,"originName"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qIndus.indusName).distinct()
                                .from(qIndus)
                                .where(qIndus.indusId.eq(qProject.projIndus))
                        ,"projIndusName"
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
                                .where(qApply.applyProjId.eq(qProject.projId).and(qApply.applyType.eq(ApplyType.APPLY.label)))
                        ,"applyCount"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qApply.applyIdx.count())
                                .from(qApply)
                                .where(qApply.applyMemId.eq(qProfile.profileMemId)
                                        .and(qApply.applyProjState.eq("4")))
                        ,"okeyCount"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qApply.applyIdx.count())
                                .from(qApply)
                                .where(qApply.applyMemId.eq(qProfile.profileMemId)
                                .and(qApply.applyProjState.eq("5")))
                        ,"contractCount"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qScrap.scrapNo.count())
                                .from(qScrap)
                                .where(qScrap.projIdx.eq(qProject.projIdx).and(qScrap.memIdx.eq(memIdx)))
                        ,"scrapCheck"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qHashtaglist.hashNm).distinct()
                                .from(qHashtaglist)
                                .where(qHashtaglist.hashId.eq(
                                        query.select(qHashtag.hashTag1).distinct()
                                                .from(qHashtag)
                                                .where(qHashtag.id.eq(qProject.projId))
                                )), "hashTag1"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qHashtaglist.hashNm).distinct()
                                .from(qHashtaglist)
                                .where(qHashtaglist.hashId.eq(
                                        query.select(qHashtag.hashTag2).distinct()
                                                .from(qHashtag)
                                                .where(qHashtag.id.eq(qProject.projId))
                                )), "hashTag2"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qHashtaglist.hashNm).distinct()
                                .from(qHashtaglist)
                                .where(qHashtaglist.hashId.eq(
                                        query.select(qHashtag.hashTag3).distinct()
                                                .from(qHashtag)
                                                .where(qHashtag.id.eq(qProject.projId))
                                )), "hashTag3"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qHashtaglist.hashNm).distinct()
                                .from(qHashtaglist)
                                .where(qHashtaglist.hashId.eq(
                                        query.select(qHashtag.hashTag4).distinct()
                                                .from(qHashtag)
                                                .where(qHashtag.id.eq(qProject.projId))
                                )), "hashTag4"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qHashtaglist.hashNm).distinct()
                                .from(qHashtaglist)
                                .where(qHashtaglist.hashId.eq(
                                        query.select(qHashtag.hashTag5).distinct()
                                                .from(qHashtag)
                                                .where(qHashtag.id.eq(qProject.projId))
                                )), "hashTag5"
                ),
                qProfile.profileChChk,
                qProfile.profileSaleChk,
                qProfile.profileBizCerti,
                qProfile.profilePhoto,
                qProfile.profileIntro,
                qMember.memNick,
                qMember.memRname))
                .from(qProject)
                .join(qMember).on(qProject.projMemId.eq(qMember.memId))
                .join(qProfile).on(qProject.projMemId.eq(qProfile.profileMemId))
                .join(qIndus).on(qProject.projIndus.eq(qIndus.indusId))
                .where(qProject.projIdx.eq(projectIdx)).fetchOne();

        return project;
    }

    private JPAQuery getProjectList(QProfile qProfile, QMember qMember,
                                         QProject qProject, QApply qApply,
                                         QHashtag qHashtag, QHashtaglist qHashtaglist,
                                         QIndus qIndus, Integer memIdx, BooleanBuilder builder, Pageable pageable) {
        JPAQuery jpaQuery = query.select(Projections.fields(Project.class,
                qProject.projIdx,
                qProject.projId,
                qProject.projMemId,
                qProject.projTitle,
                qProject.projSort,
                qProject.projIndus,
                qProject.projPrice,
                qProject.projMargin,
                qProject.projNation,
                qProject.projSupplyType,
                qProject.projRecruitNum,
                qProject.projDetail,
                qProject.projThumbnailImg,
                qProject.projProdCerti,
                qProject.projRequire,
                qProject.projState,
                qProject.projRegDate,
                qProject.projEndDate,
                qProject.projProfit,
                qProject.projChannel,
                ExpressionUtils.as(
                        JPAExpressions.select(qIndus.indusName).distinct()
                                .from(qIndus)
                                .where(qIndus.indusId.eq(qProject.projIndus))
                        ,"projIndusName"
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
                                .where(qApply.applyProjId.eq(qProject.projId).and(qApply.applyType.eq(ApplyType.APPLY.label)))
                        ,"applyCount"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qScrap.scrapNo.count())
                                .from(qScrap)
                                .where(qScrap.projIdx.eq(qProject.projIdx).and(qScrap.memIdx.eq(memIdx)))
                        ,"scrapCheck"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qHashtaglist.hashNm).distinct()
                                .from(qHashtaglist)
                                .where(qHashtaglist.hashId.eq(
                                        query.select(qHashtag.hashTag1).distinct()
                                                .from(qHashtag)
                                                .where(qHashtag.id.eq(qProject.projId))
                                )), "hashTag1"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qHashtaglist.hashNm).distinct()
                                .from(qHashtaglist)
                                .where(qHashtaglist.hashId.eq(
                                        query.select(qHashtag.hashTag2).distinct()
                                                .from(qHashtag)
                                                .where(qHashtag.id.eq(qProject.projId))
                                )), "hashTag2"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qHashtaglist.hashNm).distinct()
                                .from(qHashtaglist)
                                .where(qHashtaglist.hashId.eq(
                                        query.select(qHashtag.hashTag3).distinct()
                                                .from(qHashtag)
                                                .where(qHashtag.id.eq(qProject.projId))
                                )), "hashTag3"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qHashtaglist.hashNm).distinct()
                                .from(qHashtaglist)
                                .where(qHashtaglist.hashId.eq(
                                        query.select(qHashtag.hashTag4).distinct()
                                                .from(qHashtag)
                                                .where(qHashtag.id.eq(qProject.projId))
                                )), "hashTag4"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qHashtaglist.hashNm).distinct()
                                .from(qHashtaglist)
                                .where(qHashtaglist.hashId.eq(
                                        query.select(qHashtag.hashTag5).distinct()
                                                .from(qHashtag)
                                                .where(qHashtag.id.eq(qProject.projId))
                                )), "hashTag5"
                ),
                qProfile.profileChChk,
                qProfile.profileSaleChk,
                qProfile.profileBizCerti,
                qMember.memRname))
                .from(qProject)
                .innerJoin(qMember).on(qProject.projMemId.eq(qMember.memId))
                .innerJoin(qProfile).on(qProject.projMemId.eq(qProfile.profileMemId))
                .innerJoin(qIndus).on(qProject.projIndus.eq(qIndus.indusId))
                .where(builder)
                .orderBy(getSortedColumn(pageable.getSort(), qProject, qApply))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        return jpaQuery;
    }

    private JPAQuery selectpRegistedProjectList(QProfile qProfile, QMember qMember, QProject qProject, QApply qApply, String memId, Pageable pageable) {
        JPAQuery jpaQuery = query.select(Projections.fields(Project.class,
                qProject.projIdx,
                qProject.projId,
                qProject.projRegDate,
                qProject.projTitle,
                qProject.projEndDate,
                qProject.projRecruitNum,
                qProject.projState,
                qProject.projSort,
                qProject.projNation,
                qProject.projChannel,
                ExpressionUtils.as(
                        JPAExpressions.select(qApply.applyIdx.count())
                                .from(qApply)
                                .where(qApply.applyProjId.eq(qProject.projId).and(qApply.applyType.eq("1")))
                        ,"applyCount"
                )))
                .from(qProject)
                .join(qProject).on(qMember.memId.eq(qProject.projMemId))
                .join(qProject).on(qProfile.profileMemId.eq(qProject.projMemId))
                .where(qProject.projState.eq("1").and(qProject.projMemId.eq(memId)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qProject.projRegDate.desc());
        return jpaQuery;
    }

    private JPAQuery selectRecommandListForPro(QMember qMember, QProject qProject, QApply qApply, String memId, Pageable pageable) {
        JPAQuery jpaQuery = query.select(Projections.fields(Project.class,
                qProject.projIdx,
                qProject.projId,
                qProject.projRegDate,
                qProject.projTitle,
                qProject.projEndDate,
                qProject.projRecruitNum,
                qProject.projSort,
                qProject.projNation,
                qProject.projSupplyType,
                qProject.projChannel,
                qProject.projState,
                qApply.applyRegDate,
                qApply.applyProjState,
                qApply.applyId,
                qApply.applyIdx,
                qApply.applyProjId,
                qApply.applyType,
                qMember.memNick,
                qMember.memTel,
                qMember.memId,
                ExpressionUtils.as(
                        JPAExpressions.select(qApply.applyIdx.count())
                                .from(qApply)
                                .where(qApply.applyProjId.eq(qProject.projId).and(qApply.applyType.eq("1")))
                        ,"applyCount"
                )))
                .from(qProject)
                .join(qApply).on(qProject.projId.eq(qApply.applyProjId))
                .join(qMember).on(qApply.applyMemId.eq(qMember.memId))
                .where(qApply.applyType.eq(ApplyType.RECOMMEND.label).and(qProject.projMemId.eq(memId)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qApply.applyRegDate.desc());
        return jpaQuery;
    }

    private JPAQuery selectRecommandListForSell(QMember qMember, QProject qProject, QApply qApply, String memId, Pageable pageable) {
        JPAQuery jpaQuery = query.select(Projections.fields(Project.class,
                qProject.projIdx,
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
                qApply.applyRegDate,
                qApply.applyProjState,
                qApply.applyId,
                qApply.applyIdx,
                qApply.applyProjId,
                qApply.applyType,
                qMember.memNick,
                qMember.memTel,
                qMember.memId,
                ExpressionUtils.as(
                        JPAExpressions.select(qApply.applyIdx.count())
                                .from(qApply)
                                .where(qApply.applyProjId.eq(qProject.projId).and(qApply.applyType.eq(ApplyType.APPLY.label)))
                        ,"applyCount"
                )))
                .from(qProject)
                .join(qApply).on(qProject.projId.eq(qApply.applyProjId))
                .join(qMember).on(qProject.projMemId.eq(qMember.memId))
                .where(qApply.applyType.eq(ApplyType.RECOMMEND.label).and(qApply.applyMemId.eq(memId)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qApply.applyRegDate.desc());
        return jpaQuery;
    }

    private JPAQuery selectMyApplyList(QMember qMember, QProject qProject, QApply qApply, String memId, Pageable pageable) {
        JPAQuery jpaQuery = query.select(Projections.fields(Project.class,
                qProject.projIdx,
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
                qApply.applyRegDate,
                qApply.applyProjState,
                qApply.applyIdx,
                qApply.applyId,
                qApply.applyProjId,
                qMember.memNick,
                qMember.memTel,
                qMember.memId,
                ExpressionUtils.as(
                        JPAExpressions.select(qApply.applyIdx.count())
                                .from(qApply)
                                .where(qApply.applyProjId.eq(qProject.projId).and(qApply.applyType.eq(ApplyType.APPLY.label)))
                        ,"applyCount"
                )))
                .from(qProject)
                .join(qApply).on(qProject.projId.eq(qApply.applyProjId))
                .join(qMember).on(qProject.projMemId.eq(qMember.memId))
                .where(qApply.applyType.eq(ApplyType.APPLY.label).and(qApply.applyMemId.eq(memId)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        return jpaQuery;
    }

    private JPAQuery selectProjectEndList(QMember qMember, QProject qProject, QApply qApply, String memId, Pageable pageable) {
        JPAQuery jpaQuery = query.select(Projections.fields(Project.class,
                qProject.projIdx,
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
                qMember.memNick,
                qMember.memTel,
                ExpressionUtils.as(
                        JPAExpressions.select(qApply.applyIdx.count())
                                .from(qApply)
                                .where(qApply.applyProjId.eq(qProject.projId).and(qApply.applyType.eq(ApplyType.APPLY.label)))
                        ,"applyCount"
                )))
                .from(qProject)
                .join(qMember).on(qProject.projMemId.eq(qMember.memId))
                .where(qProject.projState.eq(ProjectStateType.END.label).and(qProject.projMemId.eq(memId)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qProject.projRegDate.desc());
        return jpaQuery;
    }
}
