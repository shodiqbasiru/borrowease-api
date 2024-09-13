package com.msfb.borrowease.repository;

import com.msfb.borrowease.entity.LoanTrx;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanTrxRepository extends JpaRepository<LoanTrx, String> {
}
