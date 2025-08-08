package com.bmilab.backend.domain.user.entity;

import com.bmilab.backend.domain.user.enums.UserPosition;
import com.bmilab.backend.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.bmilab.backend.domain.user.enums.Role;

@Entity
@Table(name = "users")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    private String organization;

    private String department;

    @Enumerated(EnumType.STRING)
    @Column(name = "position")
    private UserPosition position;

    @Column(name = "profile_image_url", columnDefinition = "TEXT")
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    private Role role;


    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateProfileImageUrl(String newProfileImageUrl) {
        this.profileImageUrl = newProfileImageUrl;
    }

    public void update(String name, String email, String organization, String department, UserPosition position) {
        this.name = name;
        this.email = email;
        this.department = department;
        this.organization = organization;
        this.position = position;
    }

    public void updateRole(Role role) {
        this.role = role;
    }

    public boolean isAdmin() {
        return Role.ADMIN.equals(role);
    }
}
