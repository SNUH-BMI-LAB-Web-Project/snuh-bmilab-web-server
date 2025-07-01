package com.bmilab.backend.domain.user.dto.query;

public record UserSearchCondition(
        String filterBy,
        String filterValue,
        String sort
) {
}