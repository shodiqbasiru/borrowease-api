package com.msfb.borrowease.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "m_job")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "job_name", nullable = false, length = 50)
    private String jobName;

    @Column(name = "company_name", nullable = false, length = 50)
    private String companyName;

    @Column(name = "salary", nullable = false)
    private int salary;
}
