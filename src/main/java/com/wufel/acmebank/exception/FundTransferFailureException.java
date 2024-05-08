package com.wufel.acmebank.exception;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public abstract class FundTransferFailureException extends RuntimeException{
    private final String sourceAccountId;
    private final String destinationAccountId;
    private final BigDecimal transferAmount;

    public FundTransferFailureException(String message, String sourceAccountId, String destinationAccountId, BigDecimal transferAmount){
        super(message);
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.transferAmount = transferAmount;
    }
}
