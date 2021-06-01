package com.sellermatch.process.board.controller;

import com.sellermatch.process.board.domain.Board;
import com.sellermatch.process.board.repository.BoardRepository;
import com.sellermatch.process.common.domain.CommonConstant;
import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.util.MailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api-v1")
public class BoardController {

    private final BoardRepository boardRepository;
    private final MailUtil mailUtil;

    @GetMapping("/board/{id}")
    public CommonDTO selectBoard(@PathVariable Integer id){
        CommonDTO result = new CommonDTO();
        boardRepository.findById(id).ifPresentOrElse(temp -> {
            result.setContent(temp);
        }, () -> {
            result.setResult("ERROR");
            result.setStatus(CommonConstant.ERROR_998);
            result.setContent(new Board());
        });
        return result;
    }
    @GetMapping("/board/list")
    public CommonDTO selectBoardList(Pageable pageable, @RequestParam List<String> boardType){
        CommonDTO result = new CommonDTO();
        result.setContent(boardRepository.findByBoardTypeIn(pageable, boardType));
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
