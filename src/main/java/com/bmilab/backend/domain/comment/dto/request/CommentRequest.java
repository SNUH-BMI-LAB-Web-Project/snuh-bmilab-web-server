package com.bmilab.backend.domain.comment.dto.request;

import com.bmilab.backend.domain.comment.enums.CommentDomainType;

public record CommentRequest(
        String message,
        CommentDomainType domainType,
        Long entityId
) {

}
