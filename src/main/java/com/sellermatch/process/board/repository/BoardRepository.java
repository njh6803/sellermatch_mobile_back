package com.sellermatch.process.board.repository;

import com.sellermatch.process.board.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BoardRepository extends PagingAndSortingRepository<Board, Integer> {

    Page<Board> findAll(Pageable pageable);

    Board findByBoardId(String boardId);

    Page<Board> findByBoardTypeIn(Pageable pageable, List<String> boardType);
}
