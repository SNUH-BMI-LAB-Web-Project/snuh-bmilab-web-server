package com.bmilab.backend.domain.project.repository;

import com.bmilab.backend.domain.project.dto.condition.ProjectFilterCondition;
import com.bmilab.backend.domain.project.dto.query.GetAllProjectsQueryResult;
import com.bmilab.backend.domain.project.dto.query.SearchProjectQueryResult;
import com.bmilab.backend.domain.project.entity.Project;
import com.bmilab.backend.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectRepositoryCustom {

    List<SearchProjectQueryResult> searchProject(Long userId, boolean all, String keyword);

    List<Project> findAllByUser(User user);
    //TODO: 연구 전체 조회 쿼리 리팩터링하기
    Page<GetAllProjectsQueryResult> findAllByFiltering(
            Long userId,
            String keyword,
            ProjectFilterCondition condition,
            Pageable pageable
    );
}