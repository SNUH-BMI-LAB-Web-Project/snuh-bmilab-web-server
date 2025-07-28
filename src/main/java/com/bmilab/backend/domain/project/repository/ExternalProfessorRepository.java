package com.bmilab.backend.domain.project.repository;

import com.bmilab.backend.domain.project.entity.ExternalProfessor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ExternalProfessorRepository extends JpaRepository<ExternalProfessor, Long>, ExternalProfessorRepositoryCustom {
    @Query("select ex from ExternalProfessor ex where :name is null "
            + "or :name = '' "
            + "or ex.name like concat('%', :name, '%')")
    List<ExternalProfessor> findAllByNameContaining(String name);

    boolean existsByNameAndOrganizationAndDepartmentAndPosition(String name, String organization, String department, String position);
}
