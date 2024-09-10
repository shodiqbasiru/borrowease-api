package com.msfb.borrowease.entity;

import com.msfb.borrowease.constant.EIdentityCard;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "m_identity_card")
public class IdentityCard {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "identity_card", nullable = false)
    @Enumerated(EnumType.STRING)
    private EIdentityCard identityCard;

    @Column(name = "identity_number", nullable = false, length = 20)
    private String identityNumber;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
}
