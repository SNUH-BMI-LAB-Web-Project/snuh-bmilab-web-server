package com.bmilab.backend.domain.project.repository;

import com.bmilab.backend.domain.project.dto.condition.ProjectFilterCondition;
import com.bmilab.backend.domain.project.dto.query.GetAllProjectsQueryResult;
import com.bmilab.backend.domain.project.dto.query.SearchProjectQueryResult;
import com.bmilab.backend.domain.project.entity.Project;
import com.bmilab.backend.domain.user.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectRepositoryCustom {
    Page<GetAllProjectsQueryResult> findAllByFiltering(Long userId, String keyword, Pageable pageable,
                                                       ProjectFilterCondition condition);

    List<SearchProjectQueryResult> searchProject(Long userId, boolean all, String keyword);

    List<Project> findAllByUser(User user);
}