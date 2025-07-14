package com.bmilab.backend.domain.user.repository;

import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.entity.UserSubAffiliation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSubAffiliationRepository extends JpaRepository<UserSubAffiliation, Long>, UserSubAffiliationRepositoryCustom {
    List<UserSubAffiliation> findAllByUser(User user);
}