package com.bmilab.backend.domain.user.dto.request;

public record UserSearchConditionRequest(
        String filterBy,
        String filterValue,
        String sort
) {
}