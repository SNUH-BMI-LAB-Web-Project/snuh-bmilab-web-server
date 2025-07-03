package com.bmilab.backend.domain.user.entity;

import com.bmilab.backend.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(name = "seat_num")
    private String seatNumber;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "joined_at")
    private LocalDate joinedAt;

    private String education;

    @Lob
    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    public void updateComment(String comment) {
        this.comment = comment;
    }

    public void update(String seatNumber, String phoneNumber) {
        this.seatNumber = seatNumber;
        this.phoneNumber = phoneNumber;
    }

    public void updateEducation(String education) {
        this.education = education;
    }
}
