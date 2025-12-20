package com.bmilab.backend.domain.research.award.entity;

import com.bmilab.backend.domain.research.entity.ResearchAuthorEntity;
import com.bmilab.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AwardRecipient extends ResearchAuthorEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "award_id", nullable = false)
    private Award award;

    @Builder
    public AwardRecipient(Award award, User user, String role) {
        super(user, role);
        this.award = award;
    }
}
