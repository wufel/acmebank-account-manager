package com.wufel.acmebank.exception;

import lombok.Getter;

import java.math.BigDecimal;

public class InsufficientFundException extends FundTransferFailureException{
    public InsufficientFundException(String sourceAccountId, String destinationAccountId, BigDecimal transferAmount){
        super(String.format("Insufficient fund from account with id %s", sourceAccountId),
                sourceAccountId, destinationAccountId, transferAmount);

    }
}
