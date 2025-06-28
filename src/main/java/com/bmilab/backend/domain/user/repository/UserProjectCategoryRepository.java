package com.bmilab.backend.domain.user.repository;

import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.entity.UserProjectCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProjectCategoryRepository extends JpaRepository<UserProjectCategory, Long> {
    List<UserProjectCategory> findAllByUser(User user);
}
