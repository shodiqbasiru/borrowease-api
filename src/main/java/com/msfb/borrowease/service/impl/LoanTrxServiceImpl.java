package com.msfb.borrowease.service.impl;

import com.msfb.borrowease.constant.ELoanInstallment;
import com.msfb.borrowease.constant.ELoanProcess;
import com.msfb.borrowease.constant.ELoanStatus;
import com.msfb.borrowease.constant.ELoanType;
import com.msfb.borrowease.entity.Customer;
import com.msfb.borrowease.entity.LoanLimit;
import com.msfb.borrowease.entity.LoanTrx;
import com.msfb.borrowease.entity.LoanTrxDetail;
import com.msfb.borrowease.model.request.LoanRequest;
import com.msfb.borrowease.model.request.PaymentRequest;
import com.msfb.borrowease.model.response.LoanResponse;
import com.msfb.borrowease.model.response.LoanTrxDetailResponse;
import com.msfb.borrowease.model.response.PaymentResponse;
import com.msfb.borrowease.repository.LoanTrxRepository;
import com.msfb.borrowease.service.CustomerService;
import com.msfb.borrowease.service.LoanLimitService;
import com.msfb.borrowease.service.LoanTrxDetailService;
import com.msfb.borrowease.service.LoanTrxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class LoanTrxServiceImpl implements LoanTrxService {

    private final LoanTrxRepository repository;
    private final CustomerService customerService;
    private final LoanLimitService loanLimitService;
    private final LoanTrxDetailService loanTrxDetailService;

    @Autowired
    public LoanTrxServiceImpl(LoanTrxRepository repository, CustomerService customerService, LoanLimitService loanLimitService, LoanTrxDetailService loanTrxDetailService) {
        this.repository = repository;
        this.customerService = customerService;
        this.loanLimitService = loanLimitService;
        this.loanTrxDetailService = loanTrxDetailService;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public LoanResponse createLoanTrx(LoanRequest request) {
        Customer customer = customerService.getById(request.getCustomerId());

        boolean isUnpaidLoan = customer.getLoanTrx().stream().anyMatch(loanTrx -> loanTrx.getLoanTrxDetails().stream().anyMatch(trxDetail -> trxDetail.getStatus().equals(ELoanStatus.UNPAID)));
        if(isUnpaidLoan) {
            throw new RuntimeException("You have unpaid loan");
        }

        double interestRate;
        ELoanInstallment loanInstallment;
        switch (request.getTermMonth()) {
            case 1 -> {
                loanInstallment = ELoanInstallment.ONE_MONTH;
                interestRate = 2.0;
            }
            case 3 -> {
                loanInstallment = ELoanInstallment.THREE_MONTH;
                interestRate = 2.5;
            }
            case 6 -> {
                loanInstallment = ELoanInstallment.SIX_MONTH;
                interestRate = 2.5;
            }
            case 12 -> {
                loanInstallment = ELoanInstallment.TWELVE_MONTH;
                interestRate = 3.0;
            }
            case 24 -> {
                loanInstallment = ELoanInstallment.TWENTY_FOUR_MONTH;
                interestRate = 3.5;
            }
            default -> throw new RuntimeException("Term month not valid");
        }

        LoanLimit loanLimit = loanLimitService.getById(customer.getLoanLimit().getId());
        if (loanLimit.getCurrentLimit() - request.getAmount() < 0) {
            throw new RuntimeException("Loan amount exceed limit");
        }
        loanLimit.setCurrentLimit(loanLimit.getCurrentLimit() - request.getAmount());
        loanLimit.setUpdatedAt(new Date());

        ELoanType loanType = getLoanType(request);

        LoanTrx loanTrx = LoanTrx.builder()
                .loanTrxDate(new Date())
                .loanType(loanType)
                .amount(request.getAmount())
                .termMonth(request.getTermMonth())
                .interestRate(interestRate)
                .installment(loanInstallment)
                .installmentAmount(request.getAmount() * (1 + interestRate / 100))
                .customer(customer)
                .loanProcess(ELoanProcess.ON_PROGRESS)
                .build();
        LoanTrx trx = repository.saveAndFlush(loanTrx);

        Double paymentAmount = request.getAmount() * (1 + interestRate / 100) / loanInstallment.getMonth();

        int lengthTrxDetail = loanInstallment.getMonth();
        List<LoanTrxDetail> loanTrxDetails = new ArrayList<>();
        for (int i = 0; i < lengthTrxDetail; i++) {
            LoanTrxDetail loanTrxDetail = LoanTrxDetail.builder()
                    .loanTrx(loanTrx)
                    .paymentAmount(paymentAmount)
                    .status(ELoanStatus.UNPAID)
                    .build();

            Date startDate = new Date();
            Date endDate = new Date(startDate.getTime() + (long) 30 * 24 * 60 * 60 * 1000);
            if (i > 0) {
                startDate = loanTrxDetails.get(i - 1).getEndDate();
                endDate = new Date(startDate.getTime() + (long) 30 * 24 * 60 * 60 * 1000);
            }

            loanTrxDetail.setStartDate(startDate);
            loanTrxDetail.setEndDate(endDate);

            LoanTrxDetail detail = loanTrxDetailService.createLoanTrxDetail(loanTrxDetail);
            loanTrxDetails.add(detail);
        }

        loanTrx.setLoanTrxDetails(loanTrxDetails);

        return LoanResponse.builder()
                .id(trx.getId())
                .customerId(trx.getCustomer().getId())
                .loanType(trx.getLoanType().name())
                .amount(trx.getAmount())
                .termMonth(trx.getTermMonth())
                .installment(trx.getInstallment().name())
                .interestRate(trx.getInterestRate() + "%")
                .installmentAmount(trx.getInstallmentAmount())
                .loanTrxDetails(trx.getLoanTrxDetails().stream().map(trxDetail -> LoanTrxDetailResponse.builder()
                        .id(trxDetail.getId())
                        .loanId(trx.getId())
                        .startDate(trxDetail.getStartDate().toString())
                        .endDate(trxDetail.getEndDate().toString())
                        .paymentAmount(trxDetail.getPaymentAmount())
                        .status(trxDetail.getStatus().name())
                        .build()).toList())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<PaymentResponse> createPaymentLoan(List<PaymentRequest> requests) {
        List<PaymentResponse> responses = new ArrayList<>();
        for (PaymentRequest request : requests) {
            LoanTrxDetail loanTrxDetail = loanTrxDetailService.getById(request.getLoanTrxDetailId());
            if (loanTrxDetail.getStatus().equals(ELoanStatus.PAID)) {
                throw new RuntimeException("Loan trx detail already paid");
            }
            if (request.getAmount() < loanTrxDetail.getPaymentAmount()) {
                throw new RuntimeException("Payment amount less than installment amount");
            }

            loanTrxDetail.setStatus(ELoanStatus.PAID);

            LoanTrxDetail trxDetail = loanTrxDetailService.createLoanTrxDetail(loanTrxDetail);
            responses.add(PaymentResponse.builder()
                            .loanTrxDetailId(trxDetail.getId())
                            .amount(request.getAmount())
                            .paymentDate(new Date().toString())
                            .status(trxDetail.getStatus().name())
                    .build());
        }
        return responses;
    }

    @Override
    public LoanTrx getById(String id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Loan trx not found"));
    }

    private static ELoanType getLoanType(LoanRequest request) {
        if (request.getLoanType().equalsIgnoreCase(ELoanType.PERSONAL.name())) {
            return ELoanType.PERSONAL;
        } else if (request.getLoanType().equalsIgnoreCase(ELoanType.BUSINESS.name())) {
            return ELoanType.BUSINESS;
        } else if (request.getLoanType().equalsIgnoreCase(ELoanType.EDUCATIONAL.name())) {
            return ELoanType.EDUCATIONAL;
        } else if (request.getLoanType().equalsIgnoreCase(ELoanType.EMERGENCY.name())) {
            return ELoanType.EMERGENCY;
        } else {
            throw new RuntimeException("Loan type not valid");
        }
    }
}
