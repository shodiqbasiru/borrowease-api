package com.msfb.borrowease.repository;

import com.msfb.borrowease.constant.ELoanTrxStatus;
import com.msfb.borrowease.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    List<Payment> findAllByTransactionStatusIn(Collection<ELoanTrxStatus> transactionStatus);
}
