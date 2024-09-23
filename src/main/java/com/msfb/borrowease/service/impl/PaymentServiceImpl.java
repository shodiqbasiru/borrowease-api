package com.msfb.borrowease.service.impl;

import com.msfb.borrowease.constant.ELoanTrxStatus;
import com.msfb.borrowease.entity.LoanLimit;
import com.msfb.borrowease.entity.LoanTrx;
import com.msfb.borrowease.entity.LoanTrxDetail;
import com.msfb.borrowease.entity.Payment;
import com.msfb.borrowease.model.request.PaymentDetailRequest;
import com.msfb.borrowease.model.request.PaymentItemDetailRequest;
import com.msfb.borrowease.model.request.PaymentLoanRequest;
import com.msfb.borrowease.model.request.PaymentRequest;
import com.msfb.borrowease.repository.PaymentRepository;
import com.msfb.borrowease.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repository;
    private final RestClient restClient;
    private final String SECRET_KEY;
    private final String SNAP_BASE_URL;

    @Autowired
    public PaymentServiceImpl(
            PaymentRepository repository,
            RestClient restClient,
            @Value("${midtrans.api.key}") String secretKey,
            @Value("${midtrans.api.snap-url}") String snapBaseUrl) {
        this.repository = repository;
        this.restClient = restClient;
        SECRET_KEY = secretKey;
        SNAP_BASE_URL = snapBaseUrl;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Payment createNewPayment(LoanTrx trx, List<PaymentLoanRequest> loanRequests) {
        double amount = 0;
        int qty = loanRequests.size();
        for (PaymentLoanRequest request : loanRequests) {
            for (LoanTrxDetail trxDetail : trx.getLoanTrxDetails()) {
                if (trxDetail.getId().equals(request.getLoanTrxDetailId())) {
                    amount += trxDetail.getPaymentAmount();
                }
            }
        }

        List<PaymentItemDetailRequest> itemDetailRequests = new ArrayList<>();
        for (int i = 0; i < qty; i++) {
            PaymentLoanRequest request = loanRequests.get(i);
            for (LoanTrxDetail trxDetail : trx.getLoanTrxDetails()) {
                if (trxDetail.getId().equals(request.getLoanTrxDetailId())) {
                    itemDetailRequests.add(PaymentItemDetailRequest.builder()
                            .name("Installment - " + trxDetail.getDueDate().toString())
                            .price(trxDetail.getPaymentAmount())
                            .quantity(1)
                            .build());
                }
            }
        }

        List<String> paymentMethods = List.of(
                "bca_va",
                "bni_va",
                "bri_va",
                "shopeepay",
                "gopay",
                "indomaret"
        );

        Payment payment = Payment.builder()
                .transactionStatus(ELoanTrxStatus.ORDERED)
                .build();

        payment = repository.saveAndFlush(payment);

        PaymentDetailRequest detailRequest = PaymentDetailRequest.builder()
                .orderId(payment.getId())
                .amount((int) amount)
                .build();

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .paymentDetail(detailRequest)
                .paymentItemDetails(itemDetailRequests)
                .paymentMethods(paymentMethods)
                .build();

        ResponseEntity<Map<String, String>> response = restClient.post()
                .uri(SNAP_BASE_URL)
                .body(paymentRequest)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + SECRET_KEY)
                .retrieve().toEntity(new ParameterizedTypeReference<>() {
                });

        Map<String, String> body = response.getBody();

        if (body == null) throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while creating payment");

        payment.setToken(body.get("token"));
        payment.setRedirectUrl(body.get("redirect_url"));
        return payment;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void checkFailedAndUpdateStatus() {
        List<ELoanTrxStatus> transactionStatus = List.of(
                ELoanTrxStatus.DENY,
                ELoanTrxStatus.CANCEL,
                ELoanTrxStatus.EXPIRE,
                ELoanTrxStatus.FAILURE
        );

        List<Payment> payments = repository.findAllByTransactionStatusIn(transactionStatus);
        for (Payment payment : payments) {
            for (LoanTrxDetail detail : payment.getLoanTrxDetails()) {
                LoanLimit limit = detail.getLoanTrx().getCustomer().getLoanLimit();
                limit.setCurrentLimit(limit.getCurrentLimit() + detail.getPaymentAmount());
            }
            payment.setTransactionStatus(ELoanTrxStatus.FAILURE);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Payment getById(String paymentId) {
        return repository.findById(paymentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"payment not found"));
    }
}
