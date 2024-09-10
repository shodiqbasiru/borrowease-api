package com.msfb.borrowease.constant;

public enum ELoanType {
    PERSONAL("Personal"),
    BUSINESS("Business"),
    EDUCATIONAL("Educational"),
    EMERGENCY("Emergency");

    private final String name;

    ELoanType(String name) {
        this.name = name;
    }
}
