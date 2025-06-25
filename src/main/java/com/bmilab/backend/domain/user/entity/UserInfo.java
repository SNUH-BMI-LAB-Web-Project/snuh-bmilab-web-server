package com.bmilab.backend.domain.user.entity;

import com.bmilab.backend.domain.project.enums.ProjectCategory;
import com.bmilab.backend.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_info")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    private String category;

    @Column(name = "seat_num")
    private String seatNumber;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "joined_at")
    private LocalDate joinedAt;

    private String education;

    private String comment;

    public void updateComment(String comment) {
        this.comment = comment;
    }

    public void update(List<ProjectCategory> categories, String seatNumber, String phoneNumber) {
        this.category = String.join(",", categories.stream().map(ProjectCategory::name).toList());
        this.seatNumber = seatNumber;
        this.phoneNumber = phoneNumber;
    }

    public void updateEducation(String education) {
        this.education = education;
    }
}
