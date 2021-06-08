package com.sellermatch.process.reply.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sellermatch.process.member.domain.QMember;
import com.sellermatch.process.project.domain.QProject;
import com.sellermatch.process.reply.domain.QReply;
import com.sellermatch.process.reply.domain.Reply;
import com.sellermatch.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ReplyRepositoryCustom {
    private final JPAQueryFactory query;
    private final QReply qReply = QReply.reply;
    private final QMember qMember = QMember.member;
    private final QProject qProject = QProject.project;

    public Page<Reply> getReplyList(Reply reply, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if (!Util.isEmpty(reply.getReplyProjId())) {
            builder.and(qReply.replyProjId.eq(reply.getReplyProjId()));
        }
        if (!Util.isEmpty(reply.getReplyBoardId())) {
            builder.and(qReply.replyBoardId.eq(reply.getReplyBoardId()));
        }
        JPAQuery jpaQuery = getReplyList(qReply, reply, pageable, builder);
        return new PageImpl<>(jpaQuery.fetch(), pageable, jpaQuery.fetchCount());
    }

    private JPAQuery getReplyList(QReply qReply, Reply reply, Pageable pageable, BooleanBuilder builder) {
        JPAQuery jpaQuery = query.select(Projections.fields(Reply.class,
                qReply.replyId,
                qReply.replyParent,
                qReply.replyContents,
                qReply.replyProjId,
                qReply.replyBoardId,
                qReply.replySecret,
                qReply.replyRegDate,
                qReply.replyDepth,
                qReply.replyParentMemId,
                qReply.replyWriter,
                ExpressionUtils.as(
                        JPAExpressions.select(qProject.projMemId)
                                .from(qProject)
                                .where(qProject.projId.eq(qReply.replyProjId))
                        ,"projMemId"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qMember.memNick)
                                .from(qMember)
                                .where(qMember.memId.eq(qReply.replyParentMemId))
                        ,"replyParentNick"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(qMember.memNick)
                                .from(qMember)
                                .where(qMember.memId.eq(qReply.replyWriter))
                        ,"replyWriterNick"
                )))
                .from(qReply)
                .where(builder)
                .orderBy(qReply.replyParent.desc());

        return jpaQuery;
    }
}
