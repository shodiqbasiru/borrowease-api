package com.msfb.borrowease.constant;

public enum ELoanInstallment {
    ONE_MONTH("One Month",1),
    THREE_MONTH("Three Month",3),
    SIX_MONTH("Six Month",6),
    TWELVE_MONTH("Twelve Month",12),
    TWENTY_FOUR_MONTH("Twenty Four Month",24);

    private final String name;
    private final Integer month;

    ELoanInstallment(String name, Integer month) {
        this.name = name;
        this.month = month;
    }

}
