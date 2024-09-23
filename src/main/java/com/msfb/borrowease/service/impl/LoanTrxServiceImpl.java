package com.msfb.borrowease.service.impl;

import com.msfb.borrowease.constant.*;
import com.msfb.borrowease.entity.*;
import com.msfb.borrowease.model.request.LoanRequest;
import com.msfb.borrowease.model.request.PaymentLoanRequest;
import com.msfb.borrowease.model.request.UpdateOrderStatusRequest;
import com.msfb.borrowease.model.response.LoanResponse;
import com.msfb.borrowease.model.response.LoanTrxDetailResponse;
import com.msfb.borrowease.model.response.PaymentDetailResponse;
import com.msfb.borrowease.model.response.PaymentResponse;
import com.msfb.borrowease.repository.LoanTrxRepository;
import com.msfb.borrowease.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class LoanTrxServiceImpl implements LoanTrxService {

    private final LoanTrxRepository repository;
    private final CustomerService customerService;
    private final LoanLimitService loanLimitService;
    private final LoanTrxDetailService loanTrxDetailService;
    private final PaymentService paymentService;

    @Autowired
    public LoanTrxServiceImpl(LoanTrxRepository repository, CustomerService customerService, LoanLimitService loanLimitService, LoanTrxDetailService loanTrxDetailService, PaymentService paymentService) {
        this.repository = repository;
        this.customerService = customerService;
        this.loanLimitService = loanLimitService;
        this.loanTrxDetailService = loanTrxDetailService;
        this.paymentService = paymentService;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public LoanResponse createLoanTrx(LoanRequest request) {
        Customer customer = customerService.getById(request.getCustomerId());

        boolean isUnpaidLoan = customer.getLoanTrx().stream().anyMatch(loanTrx -> loanTrx.getLoanTrxDetails().stream().anyMatch(trxDetail -> trxDetail.getStatus().equals(ELoanStatus.UNPAID)));
        if (isUnpaidLoan) {
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
        loanLimitService.saveLoanLimit(loanLimit);

        ELoanType loanType = getLoanType(request);

        LoanTrx loanTrx = LoanTrx.builder()
                .loanTrxDate(new Date())
                .loanType(loanType)
                .amount(request.getAmount())
                .termMonth(request.getTermMonth())
                .interestRate(interestRate)
                .installment(loanInstallment)
                .installmentAmount((int) (request.getAmount() * (1 + interestRate / 100)))
                .customer(customer)
                .loanProcess(ELoanProcess.ON_PROGRESS)
                .build();
        LoanTrx trx = repository.saveAndFlush(loanTrx);

        int paymentAmount = (int) (request.getAmount() * (1 + interestRate / 100) / loanInstallment.getMonth());

        int lengthTrxDetail = loanInstallment.getMonth();
        List<LoanTrxDetail> loanTrxDetails = new ArrayList<>();
        for (int i = 0; i < lengthTrxDetail; i++) {
            LoanTrxDetail loanTrxDetail = LoanTrxDetail.builder()
                    .loanTrx(loanTrx)
                    .paymentAmount(paymentAmount)
                    .status(ELoanStatus.UNPAID)
                    .build();

            Date startDate = new Date();
            Date dueDate = new Date(startDate.getTime() + (long) 30 * 24 * 60 * 60 * 1000);
            if (i > 0) {
                startDate = loanTrxDetails.get(i - 1).getDueDate();
                dueDate = new Date(startDate.getTime() + (long) 30 * 24 * 60 * 60 * 1000);
            }

            loanTrxDetail.setDueDate(dueDate);

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
                        .dueDate(trxDetail.getDueDate().toString())
                        .paymentAmount(trxDetail.getPaymentAmount())
                        .status(trxDetail.getStatus().name())
                        .build()).toList())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PaymentResponse createPaymentLoan(List<PaymentLoanRequest> requests) {
        List<LoanTrxDetail> loanTrxDetails = new ArrayList<>();
        for (PaymentLoanRequest request : requests) {
            LoanTrxDetail trxDetail = loanTrxDetailService.getById(request.getLoanTrxDetailId());
            if (trxDetail.getStatus().equals(ELoanStatus.PAID)) {
                throw new RuntimeException("Loan trx detail already paid");
            }
            if (request.getAmount() < trxDetail.getPaymentAmount()) {
                throw new RuntimeException("Payment amount less than installment amount");
            }

            trxDetail.setStatus(ELoanStatus.PENDING);
            loanTrxDetails.add(loanTrxDetailService.createLoanTrxDetail(trxDetail));
        }

        if (loanTrxDetails.isEmpty()) throw new RuntimeException("Loan trx detail not found");

        LoanTrx loanTrx = loanTrxDetails.get(0).getLoanTrx();
        Payment payment = paymentService.createNewPayment(loanTrx, requests);

        for (LoanTrxDetail loanTrxDetail : loanTrxDetails) {
            loanTrxDetail.setPayment(payment);
            loanTrxDetailService.createLoanTrxDetail(loanTrxDetail);
        }
        List<PaymentDetailResponse> detailPaymentResponses = requests.stream().map(request -> PaymentDetailResponse.builder()
                .id(request.getLoanTrxDetailId())
                .amount(request.getAmount())
                .build()).toList();

        if (payment == null) throw new RuntimeException("Payment not found");

        return PaymentResponse.builder()
                .id(payment.getId())
                .token(payment.getToken())
                .redirectUrl(payment.getRedirectUrl())
                .loanTrxStatus(payment.getTransactionStatus().name())
                .detailPayments(detailPaymentResponses)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public LoanTrx getById(String id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Loan trx not found"));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStatusAndLimit(UpdateOrderStatusRequest request) {
        Payment payment = paymentService.getById(request.getPaymentId());

        payment.setTransactionStatus(ELoanTrxStatus.getByName(request.getTransactionStatus()));
        payment.getLoanTrxDetails().forEach(trxDetail -> {
            if (payment.getTransactionStatus().equals(ELoanTrxStatus.SETTLEMENT)) {
                trxDetail.setStatus(ELoanStatus.PAID);
            }
        });

        LoanTrx loanTrx = payment.getLoanTrxDetails().get(0).getLoanTrx();
        boolean allPaid = loanTrx.getLoanTrxDetails().stream()
                .allMatch(detail -> detail.getStatus().equals(ELoanStatus.PAID));

        if (allPaid) {
            loanTrx.setLoanProcess(ELoanProcess.COMPLETED);
            int amount = loanTrx.getAmount();
            loanLimitService.increaseLoanLimit(loanTrx.getCustomer().getId(), amount);
        } else {
            loanTrx.setLoanProcess(ELoanProcess.ON_PROGRESS);
        }
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
