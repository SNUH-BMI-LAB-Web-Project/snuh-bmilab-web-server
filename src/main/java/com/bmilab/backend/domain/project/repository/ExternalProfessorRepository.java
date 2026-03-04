package com.bmilab.backend.domain.project.repository;

import com.bmilab.backend.domain.project.entity.ExternalProfessor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExternalProfessorRepository extends JpaRepository<ExternalProfessor, Long>, ExternalProfessorRepositoryCustom {

    List<ExternalProfessor> findAllByOrderByNameAsc();

    @Query("select ex from ExternalProfessor ex where :keyword is null "
            + "or :keyword = '' "
            + "or ex.name like concat('%', :keyword, '%') "
            + "or ex.organization like concat('%', :keyword, '%') "
            + "or ex.department like concat('%', :keyword, '%')")
    List<ExternalProfessor> findAllByKeyword(String keyword);

    boolean existsByNameAndOrganizationAndDepartmentAndPosition(String name, String organization, String department, String position);
}
