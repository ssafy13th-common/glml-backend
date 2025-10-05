package com.ssafy.a705.board.infrastructure.repository;

import com.ssafy.a705.board.domain.entity.Board;
import com.ssafy.a705.board.domain.repository.BoardRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardJpaRepository extends BoardRepository, JpaRepository<Board, Long> {

}
