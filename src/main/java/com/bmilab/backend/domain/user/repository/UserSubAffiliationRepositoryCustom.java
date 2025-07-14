package com.bmilab.backend.domain.user.repository;

import com.bmilab.backend.domain.user.dto.request.UserSubAffiliationRequest;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.entity.UserSubAffiliation;

import java.util.List;

public interface UserSubAffiliationRepositoryCustom {
    List<UserSubAffiliation> findExistsAsEntity(User user, Iterable<UserSubAffiliationRequest> keys);
}
