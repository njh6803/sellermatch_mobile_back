package com.sellermatch.process.board.controller;

import com.sellermatch.process.board.domain.Board;
import com.sellermatch.process.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BoardController {

    @Autowired
    public BoardRepository boardRepository;

    @GetMapping("/board")
    public List<Board> selectBoard(){
        return boardRepository.findAll();
    }
}
