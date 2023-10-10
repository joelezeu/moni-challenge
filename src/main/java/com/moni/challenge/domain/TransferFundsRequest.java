package com.moni.challenge.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferFundsRequest {
    private String reference;
    private String debitPhoneNumber;
    private String creditPhoneNumber;
    private BigDecimal amount;
}
