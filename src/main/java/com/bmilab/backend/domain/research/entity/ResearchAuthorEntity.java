package com.bmilab.backend.domain.research.entity;

import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ResearchAuthorEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String role;

    protected ResearchAuthorEntity(User user, String role) {
        this.user = user;
        this.role = role;
    }

    public void updateRole(String role) {
        this.role = role;
    }
}
