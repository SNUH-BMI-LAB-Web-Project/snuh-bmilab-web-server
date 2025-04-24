package com.bmilab.backend.domain.user.repository;

import com.amazonaws.services.s3.model.RequestPaymentConfiguration.Payer;
import com.bmilab.backend.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByName(String name);

    @Query("select u from User u where u.id in :ids")
    List<User> findAllByIds(Set<Long> ids);
}
