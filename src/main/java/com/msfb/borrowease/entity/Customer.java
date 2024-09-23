package com.msfb.borrowease.entity;

import com.msfb.borrowease.constant.EEducation;
import com.msfb.borrowease.constant.EStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "m_customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "mother_name", nullable = false, length = 50)
    private String motherName;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private EStatus status;

    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "birth_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date birthDate;

    @Column(name = "address")
    private String address;

    @Column(name = "last_education", nullable = false)
    @Enumerated(EnumType.STRING)
    private EEducation lastEducation;

    @Column(name = "npwp", nullable = false, length = 50)
    private String npwp;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @OneToOne
    @JoinColumn(name = "job_id", unique = true)
    private Job job;

    @OneToOne
    @JoinColumn(name = "loan_limit_id", unique = true)
    private LoanLimit loanLimit;

    @OneToMany(mappedBy = "customer")
    private List<IdentityCard> identityCards;

    @OneToMany(mappedBy = "customer")
    private List<LoanTrx> loanTrx;

}
