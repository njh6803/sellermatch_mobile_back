package com.sellermatch.process.board.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sellermatch.process.board.domain.Board;
import com.sellermatch.process.board.domain.QBoard;
import com.sellermatch.process.reply.domain.QReply;
import com.sellermatch.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class BoardRepositoryCustom {
    private final JPAQueryFactory query;
    private final QBoard qBoard = QBoard.board;
    private final QReply qReply = QReply.reply;

    public Page<Board> getBoardList(List<String> boardType, String boardQaType, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();

        if (!Util.isEmpty(boardType)) {
            builder.and(qBoard.boardType.in(boardType));
        }
        if (!Util.isEmpty(boardQaType)) {
            builder.and(qBoard.boardQaType.eq(boardQaType));
        }

        JPAQuery jpaQuery = selectBoardList(qBoard, pageable, builder);

        return new PageImpl<>(jpaQuery.fetch(), pageable, jpaQuery.fetchCount());
    }

    public Page<Board> getBoardNoticeTopList(Pageable pageable) {
        JPAQuery jpaQuery = selectBoardNoticeTopList(qBoard, pageable);

        return new PageImpl<>(jpaQuery.fetch(), pageable, jpaQuery.fetchCount());
    }

    private JPAQuery selectBoardList(QBoard qBoard, Pageable pageable, BooleanBuilder builder) {

        JPAQuery jpaQuery = query.select(Projections.fields(Board.class,
                qBoard.boardIdx,
                qBoard.boardId,
                qBoard.boardTitle,
                qBoard.boardContents,
                qBoard.boardWriter,
                qBoard.boardType,
                qBoard.boardQaType,
                qBoard.boardEmail,
                qBoard.boardFile,
                qBoard.boardRegDate,
                qBoard.boardEditDate,
                qBoard.boardNoticeTop,
                qBoard.memNick,
                qBoard.memSort,
                ExpressionUtils.as(
                        JPAExpressions.select(qReply.replyId.count())
                                .from(qReply)
                                .where(qReply.replyBoardId.eq(qBoard.boardId))
                        ,"replyCount"
                )))
                .from(qBoard)
                .where(builder.and(qBoard.boardNoticeTop.eq("N")))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qBoard.boardRegDate.desc());

        return jpaQuery;
    }

    private JPAQuery selectBoardNoticeTopList(QBoard qBoard, Pageable pageable) {

        JPAQuery jpaQuery = query.select(Projections.fields(Board.class,
                qBoard.boardIdx,
                qBoard.boardId,
                qBoard.boardTitle,
                qBoard.boardContents,
                qBoard.boardWriter,
                qBoard.boardType,
                qBoard.boardQaType,
                qBoard.boardEmail,
                qBoard.boardFile,
                qBoard.boardRegDate,
                qBoard.boardEditDate,
                qBoard.boardNoticeTop,
                ExpressionUtils.as(
                        JPAExpressions.select(qReply.replyId.count())
                                .from(qReply)
                                .where(qReply.replyBoardId.eq(qBoard.boardId))
                        ,"replyCount"
                )))
                .from(qBoard)
                .where(qBoard.boardNoticeTop.eq("Y"))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qBoard.boardRegDate.desc());
        return jpaQuery;
    }
}
