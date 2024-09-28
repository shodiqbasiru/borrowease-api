package com.msfb.borrowease.service.impl;

import com.msfb.borrowease.constant.*;
import com.msfb.borrowease.entity.*;
import com.msfb.borrowease.mapping.LoanTrxMapping;
import com.msfb.borrowease.model.request.LoanApprovalRequest;
import com.msfb.borrowease.model.request.LoanRequest;
import com.msfb.borrowease.model.request.PaymentLoanRequest;
import com.msfb.borrowease.model.request.UpdateOrderStatusRequest;
import com.msfb.borrowease.model.response.*;
import com.msfb.borrowease.repository.LoanTrxRepository;
import com.msfb.borrowease.service.*;
import com.msfb.borrowease.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LoanTrxServiceImpl implements LoanTrxService {

    private final LoanTrxRepository repository;
    private final CustomerService customerService;
    private final LoanLimitService loanLimitService;
    private final LoanTrxDetailService loanTrxDetailService;
    private final PaymentService paymentService;

    private static final double LATE_FEE_PERCENTAGE = 0.05;

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
    public ApplicationResponse createLoanApplication(LoanRequest request) {
        Customer customer = customerService.getById(request.getCustomerId());

        boolean isUnpaidLoan = customer.getLoanTrx().stream().anyMatch(loanTrx -> loanTrx.getLoanTrxDetails().stream().anyMatch(trxDetail -> trxDetail.getStatus().equals(ELoanStatus.UNPAID)));
        if (isUnpaidLoan) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Customer has unpaid loan");
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
            default -> throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Term month not valid");
        }

        LoanLimit loanLimit = loanLimitService.getById(customer.getLoanLimit().getId());
        if (loanLimit.getCurrentLimit() - request.getAmount() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Loan amount exceeds loan limit");
        }

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
                .loanProcess(ELoanProcess.PENDING)
                .build();
        repository.saveAndFlush(loanTrx);


        return ApplicationResponse.builder()
                .id(loanTrx.getId())
                .customerId(loanTrx.getCustomer().getId())
                .loanType(loanTrx.getLoanType().name())
                .amount(loanTrx.getAmount())
                .loanProcess(loanTrx.getLoanProcess().name())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public LoanResponse createLoanApproval(LoanApprovalRequest request) {
        LoanTrx loanTrx = getById(request.getLoanId());

        if (request.getLoanProcess().equalsIgnoreCase(ELoanProcess.REJECTED.name())) {
            loanTrx.setLoanProcess(ELoanProcess.REJECTED);
            return LoanTrxMapping.toLoanResponse(loanTrx);
        } else if (request.getLoanProcess().equalsIgnoreCase(ELoanProcess.APPROVED.name())) {

            if (loanTrx.getLoanProcess().equals(ELoanProcess.APPROVED)) {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Loan already approved");
            }

            loanTrx.setLoanProcess(ELoanProcess.APPROVED);
            LoanLimit loanLimit = loanLimitService.getById(loanTrx.getCustomer().getLoanLimit().getId());
            loanLimit.setCurrentLimit(loanLimit.getCurrentLimit() - loanTrx.getAmount());
            loanLimit.setUpdatedAt(new Date());
            loanLimitService.saveLoanLimit(loanLimit);

            int paymentAmount = (int) (loanTrx.getAmount() * (1 + loanTrx.getInterestRate() / 100) / loanTrx.getInstallment().getMonth());

            int lengthTrxDetail = loanTrx.getInstallment().getMonth();
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
            return LoanTrxMapping.toLoanResponse(loanTrx);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Loan process not valid");
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PaymentResponse createPaymentLoan(List<PaymentLoanRequest> requests) {
        List<LoanTrxDetail> loanTrxDetails = new ArrayList<>();
        for (PaymentLoanRequest request : requests) {
            LoanTrxDetail trxDetail = loanTrxDetailService.getById(request.getLoanTrxDetailId());
            if (trxDetail.getStatus().equals(ELoanStatus.PAID)) {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Loan trx detail already paid");
            }
            if (request.getAmount() < trxDetail.getPaymentAmount()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment amount less than payment amount");
            }
            if (DateUtil.isLatePayment(trxDetail.getDueDate())) {
                trxDetail.setLateFee(LATE_FEE_PERCENTAGE * trxDetail.getPaymentAmount());
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
        List<PaymentDetailResponse> detailPaymentResponses = requests.stream().map(request -> {
            Optional<LoanTrxDetail> optionalTrxDetail = loanTrxDetails.stream()
                    .filter(trxDetail -> trxDetail.getId().equals(request.getLoanTrxDetailId()))
                    .findFirst();
            if (optionalTrxDetail.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Loan trx detail not found");

            String status = optionalTrxDetail.get().getStatus().name();
            double lateFee = optionalTrxDetail.get().getLateFee() != null ? optionalTrxDetail.get().getLateFee() : 0;

            return PaymentDetailResponse.builder()
                    .id(request.getLoanTrxDetailId())
                    .amount(request.getAmount())
                    .status(status)
                    .lateFee(lateFee)
                    .build();
        }).toList();

        if (payment == null) throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while creating payment");

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
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Loan trx not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<LoanResponse> getAllLoanTrx() {
        List<LoanTrx> loans = repository.findAll();
        return LoanTrxMapping.toLoanResponses(loans);
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

    @Transactional(readOnly = true)
    @Override
    public LoanResponse getLoanTrxById(String id) {
        LoanTrx loanTrx = getById(id);
        return LoanTrxMapping.toLoanResponse(loanTrx);
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
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Loan type not valid");
        }
    }
}
