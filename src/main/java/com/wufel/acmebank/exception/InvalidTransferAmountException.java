package com.wufel.acmebank.exception;

import java.math.BigDecimal;

public class InvalidTransferAmountException extends FundTransferFailureException{
    public InvalidTransferAmountException(String sourceAccountId, String destinationAccountId, BigDecimal transferAmount) {
        super(String.format("transfer amount %s is not valid", transferAmount.toString()),
                sourceAccountId, destinationAccountId, transferAmount);
    }
}
