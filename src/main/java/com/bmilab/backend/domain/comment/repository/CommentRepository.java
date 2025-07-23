package com.bmilab.backend.domain.comment.repository;

import com.bmilab.backend.domain.comment.entity.Comment;
import com.bmilab.backend.domain.comment.enums.CommentDomainType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByDomainTypeAndEntityId(CommentDomainType domainType, Long entityId);
}
