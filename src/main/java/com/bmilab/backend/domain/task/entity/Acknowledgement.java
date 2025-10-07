package com.bmilab.backend.domain.task.entity;

import com.bmilab.backend.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "acknowledgements")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Acknowledgement extends BaseTimeEntity {

    @Id
    @Column(name = "acknowledgement_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Task task;

    @Column(name = "acknowledgement_text", columnDefinition = "TEXT", nullable = false)
    private String acknowledgementText;

    @Column(name = "related_info", columnDefinition = "TEXT")
    private String relatedInfo;

    @Column(name = "related_link", columnDefinition = "TEXT")
    private String relatedLink;

    public void update(String acknowledgementText, String relatedInfo, String relatedLink) {
        this.acknowledgementText = acknowledgementText;
        this.relatedInfo = relatedInfo;
        this.relatedLink = relatedLink;
    }
}
