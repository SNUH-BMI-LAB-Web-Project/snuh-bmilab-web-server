package com.bmilab.backend.domain.user.dto.response;

import com.bmilab.backend.domain.user.entity.UserSubAffiliation;
import lombok.Builder;

@Builder
public record UserSubAffiliationSummary(
        String organization,

        String department,

        String position
) {
    public static UserSubAffiliationSummary from(UserSubAffiliation userSubAffiliation) {
        return UserSubAffiliationSummary
                .builder()
                .organization(userSubAffiliation.getOrganization())
                .department(userSubAffiliation.getDepartment())
                .position(userSubAffiliation.getPosition())
                .build();
    }
}