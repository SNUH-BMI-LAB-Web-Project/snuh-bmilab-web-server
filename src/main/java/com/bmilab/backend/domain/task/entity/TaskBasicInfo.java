package com.bmilab.backend.domain.task.entity;

import com.bmilab.backend.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "task_basic_info")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TaskBasicInfo extends BaseTimeEntity {

    @Id
    @Column(name = "task_basic_info_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column(name = "ministry")
    private String ministry;

    @Column(name = "specialized_agency")
    private String specializedAgency;

    @Column(name = "announcement_number")
    private String announcementNumber;

    @Column(name = "business_contact_name")
    private String businessContactName;

    @Column(name = "business_contact_department")
    private String businessContactDepartment;

    @Column(name = "business_contact_email")
    private String businessContactEmail;

    @Column(name = "business_contact_phone")
    private String businessContactPhone;

    @Column(name = "announcement_link", columnDefinition = "TEXT")
    private String announcementLink;

    public void update(
            String ministry,
            String specializedAgency,
            String announcementNumber,
            String businessContactName,
            String businessContactDepartment,
            String businessContactEmail,
            String businessContactPhone,
            String announcementLink
    ) {
        this.ministry = ministry;
        this.specializedAgency = specializedAgency;
        this.announcementNumber = announcementNumber;
        this.businessContactName = businessContactName;
        this.businessContactDepartment = businessContactDepartment;
        this.businessContactEmail = businessContactEmail;
        this.businessContactPhone = businessContactPhone;
        this.announcementLink = announcementLink;
    }
}
