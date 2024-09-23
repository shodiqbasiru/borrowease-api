package com.msfb.borrowease.scheduler;

import com.msfb.borrowease.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentStatusScheduler {
    private final PaymentService paymentService;

    @Autowired
    public PaymentStatusScheduler(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Scheduled(fixedRate = 60000)
    public void checkFailedPayments() {
        log.info("START checkFailedPayments() : {}", System.currentTimeMillis());
        paymentService.checkFailedAndUpdateStatus();
        log.info("END checkFailedPayments() : {}", System.currentTimeMillis());
    }
}
