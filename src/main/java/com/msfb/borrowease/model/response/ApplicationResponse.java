package com.msfb.borrowease.model.response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationResponse {
    private String id;
    private String customerId;
    private String loanType;
    private int amount;
    private String loanProcess;
}
