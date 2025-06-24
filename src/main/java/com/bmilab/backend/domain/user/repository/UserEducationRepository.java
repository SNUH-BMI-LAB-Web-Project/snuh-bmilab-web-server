package com.bmilab.backend.domain.user.repository;

import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.entity.UserEducation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEducationRepository extends JpaRepository<UserEducation, Long> {
    List<UserEducation> findAllByUser(User user);

    List<UserEducation> findAllByUserId(Long userId);
}
