package com.msfb.borrowease.repository;

import com.msfb.borrowease.entity.LoanTrxDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanTrxDetailRepository extends JpaRepository<LoanTrxDetail, String> {
}
