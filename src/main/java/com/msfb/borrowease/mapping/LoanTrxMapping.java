package com.msfb.borrowease.mapping;

import com.msfb.borrowease.entity.LoanTrx;
import com.msfb.borrowease.model.response.LoanResponse;
import com.msfb.borrowease.model.response.LoanTrxDetailResponse;

import java.util.List;

public class LoanTrxMapping {
    public static LoanResponse toLoanResponse(LoanTrx trx) {
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

    public static List<LoanResponse> toLoanResponses(List<LoanTrx> loans) {
        return loans.stream().map(LoanTrxMapping::toLoanResponse).toList();
    }
}
