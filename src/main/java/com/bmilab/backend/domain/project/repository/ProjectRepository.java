package com.bmilab.backend.domain.project.repository;

import com.bmilab.backend.domain.project.entity.Project;
import com.bmilab.backend.domain.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long>, ProjectRepositoryCustom {
}
