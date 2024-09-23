package com.msfb.borrowease.constant;

import java.util.Arrays;

public enum ELoanTrxStatus {
    ORDERED("ordered"),
    PENDING("pending"),
    SETTLEMENT("settlement"),
    CANCEL("cancel"),
    DENY("deny"),
    EXPIRE("expire"),
    FAILURE("failure");

    private final String name;

    ELoanTrxStatus(String name) {
        this.name = name;
    }

    public static ELoanTrxStatus getByName(String name) {
        return Arrays.stream(values())
                .filter(loanStatus -> loanStatus.name.equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}