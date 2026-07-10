package com.example.ats.infrastructure.persistence.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "recruiters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecruiterEntity {
    @Id
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    @MapsId
    private UserEntity user;
    @ManyToOne()
    @JoinColumn(name = "company_id")
    private CompanyEntity company;
    @Column
    private  String position;
    @OneToMany(mappedBy = "interviewer")
    private List<InterviewEntity> interviews;
    @OneToMany(mappedBy = "reviewer")
    private List<InterviewFeedbackEntity> feedbacks;

    public RecruiterEntity(Long id, UserEntity user, CompanyEntity company, String position) {
        this.id = id;
        this.user = user;
        this.company = company;
        this.position = position;
    }
}
