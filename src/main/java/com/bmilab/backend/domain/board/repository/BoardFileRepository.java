package com.bmilab.backend.domain.board.repository;

import com.bmilab.backend.domain.board.entity.BoardFile;
import com.bmilab.backend.domain.file.entity.FileInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BoardFileRepository extends JpaRepository<BoardFile, UUID> {
    Optional<BoardFile> findByFileInformation(FileInformation file);


    @Query(
            "SELECT bf FROM BoardFile bf " +
                    "WHERE bf.fileInformation.domainType = com.bmilab.backend.domain.file.enums.FileDomainType.BOARD " +
                    "AND bf.fileInformation.entityId = :boardId"
    )
    List<BoardFile> findAllByBoardId(@Param("boardId") Long boardId);
}
