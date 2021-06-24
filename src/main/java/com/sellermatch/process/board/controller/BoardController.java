package com.sellermatch.process.board.controller;

import com.sellermatch.process.board.domain.Board;
import com.sellermatch.process.board.repository.BoardRepository;
import com.sellermatch.process.board.repository.BoardRepositoryCustom;
import com.sellermatch.process.common.domain.CommonConstant;
import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.util.ControllerResultSet;
import com.sellermatch.util.MailUtil;
import com.sellermatch.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api-v1")
public class BoardController {

    private final BoardRepository boardRepository;
    private final MailUtil mailUtil;
    private final BoardRepositoryCustom boardRepositoryCustom;

    @GetMapping("/board/{id}")
    public CommonDTO selectBoard(@PathVariable Integer id){
        CommonDTO result = new CommonDTO();
        boardRepository.findById(id).ifPresentOrElse(temp -> {
            if (Util.isEmpty(temp.getMemNick())) {
                temp.setMemNick("관리자");
            }
            result.setContent(temp);
        }, () -> {
            Board emptyContent =  new Board();
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_998, emptyContent);
        });
        return result;
    }
    @GetMapping("/board/list")
    public CommonDTO selectBoardList(Pageable pageable, @RequestParam List<String> boardType, @RequestParam(required = false) String boardQaType){
        CommonDTO result = new CommonDTO();
        Page<Board> board = boardRepositoryCustom.getBoardList(boardType, boardQaType, pageable);
        for (int i = 0; i < board.getTotalElements(); i++) {
            if (Util.isEmpty(board.getContent().get(i).getMemNick())) {
                board.getContent().get(i).setMemNick("관리자");
            }
        }
        result.setContent(board);
        return result;
    }

    @PostMapping("/board")
    public CommonDTO insertBoard(@RequestBody Board board){
        CommonDTO result = new CommonDTO();
        // 게시판 유형 null
        if (Util.isEmpty(board.getBoardType())) {
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_231);
            return result;
        }
        // 게시판 내용 null
        if (Util.isEmpty(board.getBoardContents())) {
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_230);
            return result;
        }
        // 게시판 길이 (1000자 이하)
        if (Util.isLengthChk(board.getBoardContents(), 0, 1000)) {
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_LENGTH_232);
            return result;
        }
        // 게시판 제목 null
        if (Util.isEmpty(board.getBoardTitle())) {
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_132);
            return result;
        }

        board.setBoardId(Util.getUniqueId("B-", Integer.parseInt(board.getBoardType())));
        board.setBoardRegDate(new Date());
        result.setContent(boardRepository.save(board));
        return result;
    }

    @PutMapping("/board")
    public CommonDTO updateBoard(@RequestBody Board board){
        CommonDTO result = new CommonDTO();
        // 게시판 유형 null
        if (Util.isEmpty(board.getBoardType())) {
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_231);
            return result;
        }
        // 게시판 내용 null
        if (Util.isEmpty(board.getBoardContents())) {
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_230);
            return result;
        }
        // 게시판 길이 (1000자 이하)
        if (Util.isLengthChk(board.getBoardContents(), 0, 1000)) {
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_LENGTH_232);
            return result;
        }
        // 게시판 제목 null
        if (Util.isEmpty(board.getBoardTitle())) {
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_NULL_132);
            return result;
        }
        boardRepository.findById(board.getBoardIdx()).ifPresent(temp -> {
            temp.setBoardTitle(board.getBoardTitle());
            temp.setBoardContents(board.getBoardContents());
            temp.setBoardEditDate(new Date());
            result.setContent(boardRepository.save(temp));
        });
        return result;
    }

/*    @DeleteMapping("/board")
    public CommonDTO deleteBoard(Board board){
        CommonDTO result = new CommonDTO();
        boardRepository.findById(board.getBoardIdx()).ifPresentOrElse(temp -> {
            boardRepository.delete(board);
        }, () -> {});
        return result;
    }*/
}
