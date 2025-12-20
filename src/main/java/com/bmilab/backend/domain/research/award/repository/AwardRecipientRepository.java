package com.bmilab.backend.domain.research.award.repository;

import com.bmilab.backend.domain.research.award.entity.AwardRecipient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AwardRecipientRepository extends JpaRepository<AwardRecipient, Long> {
    List<AwardRecipient> findAllByAwardId(Long awardId);
    void deleteAllByAwardId(Long awardId);
}
