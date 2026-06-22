package com.example.ats.infrastructure.persistence.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "recuiters")
@Getter
@Setter
@NoArgsConstructor
public class RecuiterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
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
}
