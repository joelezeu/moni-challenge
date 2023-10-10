package com.moni.challenge.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FundWalletRequest {
    private String reference;
    private String phoneNumber;
    private BigDecimal amount;
}
