package com.bmilab.backend.domain.board.repository;

import com.bmilab.backend.domain.board.entity.Board;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Board b WHERE b.id = :boardId")
    Optional<Board> findByIdWithPessimisticLock(@Param("boardId") Long boardId);

    long countByIsPinned(Boolean isPinned);
}
