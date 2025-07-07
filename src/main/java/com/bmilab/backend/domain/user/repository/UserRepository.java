package com.bmilab.backend.domain.user.repository;

import com.bmilab.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    Optional<User> findByEmail(String email);

    boolean existsByName(String name);

    @Query("select u from User u where u.id in :ids")
    List<User> findAllByIds(Set<Long> ids);

    @Query("SELECT u FROM User u WHERE :name IS NULL "
            + "OR :name = '' "
            + "OR u.name LIKE CONCAT('%', :name, '%') "
            + "OR u.email LIKE CONCAT('%', :name, '%') "
            + "OR u.department LIKE CONCAT('%', :name, '%')")
    List<User> searchUsersByKeyword(String name);

    boolean existsByEmail(String email);
}
