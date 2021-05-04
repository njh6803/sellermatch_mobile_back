package com.sellermatch.process.board.repository;

import com.sellermatch.process.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Integer> {
}
