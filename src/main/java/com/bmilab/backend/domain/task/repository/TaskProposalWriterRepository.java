package com.bmilab.backend.domain.task.repository;

import com.bmilab.backend.domain.task.entity.TaskProposal;
import com.bmilab.backend.domain.task.entity.TaskProposalWriter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskProposalWriterRepository extends JpaRepository<TaskProposalWriter, Long> {
    List<TaskProposalWriter> findByTaskProposal(TaskProposal taskProposal);
    void deleteByTaskProposal(TaskProposal taskProposal);
}
