package com.sellermatch.process.board.controller;

import com.sellermatch.process.board.domain.Board;
import com.sellermatch.process.board.repository.BoardRepository;
import com.sellermatch.util.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public Page<Board> selectBoard(){
        Pageable pageable = PageRequest.of(0,1);
        return boardRepository.findAll(pageable);
    }
    @GetMapping("/board/list")
    public Page<Board> selectBoardList(Pageable pageable){
        return boardRepository.findAll(pageable);
    }

    @PostMapping("/board")
    public Board insertBoard(Board board){
        return boardRepository.save(board);
    }

    @PutMapping("/board")
    public Board updateBoard(Board board){
        boardRepository.findById(board.getBoardIdx()).ifPresentOrElse(temp -> {
            boardRepository.save(board);
        }, () -> {});
        return board;
    }

    @DeleteMapping("/board")
    public Board deleteBoard(Board board){
        boardRepository.findById(board.getBoardIdx()).ifPresentOrElse(temp -> {
            boardRepository.delete(board);
        }, () -> {});
        return board;
    }
}
