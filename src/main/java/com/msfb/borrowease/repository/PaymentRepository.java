package com.msfb.borrowease.repository;

import com.msfb.borrowease.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, String> {
}
