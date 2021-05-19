package com.sellermatch.process.board.controller;

import com.sellermatch.process.board.domain.Board;
import com.sellermatch.process.board.repository.BoardRepository;
import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.util.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
public class BoardController {

    @Autowired
    public BoardRepository boardRepository;

    @Autowired
    public MailUtil mailUtil;

    @GetMapping("/board")
    public CommonDTO selectBoard(){
        CommonDTO result = new CommonDTO();
        Pageable pageable = PageRequest.of(0,1);
        result.setContent(boardRepository.findAll(pageable));
        return result;
    }
    @GetMapping("/board/list")
    public CommonDTO selectBoardList(Pageable pageable){
        CommonDTO result = new CommonDTO();
        result.setContent(boardRepository.findAll(pageable));
        return result;
    }

    @PostMapping("/board")
    public CommonDTO insertBoard(@RequestBody Board board){
        CommonDTO result = new CommonDTO();
        result.setContent(boardRepository.save(board));
        return result;
    }

    @PutMapping("/board")
    public CommonDTO updateBoard(@RequestBody Board board){
        CommonDTO result = new CommonDTO();
        boardRepository.findById(board.getBoardIdx()).ifPresentOrElse(temp -> {
            result.setContent(boardRepository.save(board));
        }, () -> {});
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
