package com.msfb.borrowease.repository;

import com.msfb.borrowease.entity.LoanLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanLimitRepository extends JpaRepository<LoanLimit, String> {
}
